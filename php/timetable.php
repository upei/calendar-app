<?php

define('CACHE_DIRECTORY', 'cache');
include_once('../libs/cache.php');

$cache = new WIFileCache(CACHE_DIRECTORY);

define('URL', 'http://ic.upei.ca/services/xml/xslt.php?_type=timetable');

define('DB_HOST', '127.0.0.1');
define('DB_PORT', 3306);
define('DB_NAME', 'CourseApp');
define('DB_USER', 'root');
define('DB_PASS', '');

$ttdb_ = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME, DB_PORT);

function cache_get($url) {
	global $cache;
	
	$key = md5($url);
	if ($content = $cache->get($key)) {
		return simplexml_load_string($content);
	}
	else {
		$content = file_get_contents($url);
		$cache->put($key, $content);
		return simplexml_load_string($content);
	}
}

function get_departments($params) {
	$xml = cache_get(URL);
	// $xml->registerXPathNamespace('tt', 'http://upei.ca/xsd/timetable');
	$list = array_slice(array_unique($xml->xpath('//node/data/department')), 0);
	
	$departments = array();
	foreach ($list as $d) {
		$departments[] = strval($d);
	}
	
	return $departments;
}

function get_semesters($params) {
	// The semesters are hard-coded here to alleviate the server load.
	return array(
		"First Semester",
		"Second Semester",
		"First Summer School",
		"Second Summer School"
		);
}

function get_course_levels($params) {
	// The course levels are hard-coded here to alleviate the server load.
	return array(
		"000",
		"100",
		"200",
		"300",
		"400",
		"500",
		"600",
		// "700",
		// "800",
		);
}

function get_courses_of_departments_and_levels($params) {
	$departments = $params->department;
	$levels = $params->level;
	$semesters = $params->semester;
	
	$xml = cache_get(URL);
	// $xml->registerXPathNamespace('tt', 'http://upei.ca/xsd/timetable');

	// parse departments
	$department_array = array("false");
	foreach ($departments as $key => $value) {
		if ($value) {
			$department_array[] = "department=\"$key\"";
		}
	}
	
	$department_predicate = "";
	if (!empty($department_array)) {
		$department_predicate = join(" or ", $department_array);
	}

	// var_dump($predicate);
	$semester_array = array("false");
	foreach ($semesters as $key => $value) {
		if ($value) {
			$semester_array[] = "semester=\"$key\"";
		}
	}
	
	$semester_predicate = "";
	if (!empty($semester_array)) {
		$semester_predicate = join(" or ", $semester_array);
	}
	
	// var_dump($semester_predicate);

	$list = $xml->xpath("//node/data[($department_predicate) and ($semester_predicate)]");

	// parse levels
	$level_predicate = "[";
	foreach ($levels as $key => $value) {
		if ($value) {
			$level_predicate .= $key[0];
		}
	}
	$level_predicate .= "]";
	
	$courses = array();
	foreach($list as $course) {
		if (preg_match("/^[A-Z -]{3,4}$level_predicate\d{2,2}/", $course->name)) {
			$courses[] = array(
				'id' => intval($course->id),
				'name' => strval($course->name),
				'location' => strval($course->location),
				'title' => strval($course->title),
				'time' => strval($course->time),
				);
		}
	}
	
	return $courses;
}

function __get_course_description($department, $number) {
	global $ttdb_;
	
	if ($desc_stmt = $ttdb_->prepare('SELECT description FROM course_full_view WHERE (program_name=? or program_name2=?) and (number=? or number2=?)')) {
	
		// bind parameters
		$desc_stmt->bind_param('ssii', $department, $department, $number, $number);
		
		// execute the statement
		$desc_stmt->execute();
		
		// bind the result
		$desc_stmt->bind_result($description);
		
		if ($desc_stmt->fetch()) {
			return $description;
		}
	}
	
	return '';
}

function get_course_detail($params) {
	$id = $params->id;
	
	// get the xml output
	$xml = cache_get(URL);
	// $xml->registerXPathNamespace('tt', 'http://upei.ca/xsd/timetable');
	
	$list = $xml->xpath("//node[@id='tt-$id']");
	
	$course = array();

	if (count($list) == 0) {
		$course['sucess'] = false;
		return $course;
	}
	else {
		$c = $list[0]->data;
		$course['success'] = true;
		$course['id'] = intval($id);
		$course['name'] = strval($c->name);
		$course['title'] = strval($c->title);
		$course['semester'] = strval($c->semester);
		$course['location'] = strval($c->location);
		$course['status'] = strval($c->status);
		if (empty($course['status'])) $course['status'] = 'Open';
		
		$course['time'] = strval($c->time);
		$course['department'] = strval($c->department);
		
		// rip out the number from the name
		if (preg_match('/^[A-Z -]{3,4}(\d{3,3})[A-Z]?/', $course['name'], $matches)) {
			$course['description'] = __get_course_description($course['department'], $matches[1]);
		}
		else {
			$course['description'] = '';
		}
		
		// $c->registerXPathNamespace('tt', 'http://upei.ca/xsd/timetable');
		$teachers = array();
		foreach($c->xpath('instructors/name') as $instructor) {
			$teachers[] = strval($instructor);
		}
		$course['instructors'] = join("; ", $teachers);
		return $course;
	}
}

if (!isset($_GET['method'])) {
	die('method name invalid!');
}

$method = $_GET['method'];
unset($_GET['method']);

// get the post content
// input must not be application/x-www.form-urlencoded or multipart/form-data

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	$params = json_decode(file_get_contents('php://input'));
}
else {
	$params = array();
	foreach ($_GET as $name => $value) {
		$params[$name] = $value;
	}
}

header('Content-type: application/json');
echo json_encode($method($params));


// close the database connection
$ttdb_->close();

 