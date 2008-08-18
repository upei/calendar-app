<?php

define('URL', 'https://secure.upei.ca/cls/dropbox/full-timetable.xml');

function cache_get($url) {
	return simplexml_load_file($url);
}

function get_departments($params) {
	$xml = cache_get(URL);
	$xml->registerXPathNamespace('tt', 'http://upei.ca/xsd/timetable');
	$list = array_slice(array_unique($xml->xpath('//tt:course/tt:department')), 0);
	
	$departments = array();
	foreach ($list as $d) {
		$departments[] = strval($d);
	}
	
	return $departments;
}

function get_semesters($params) {
	// The semesters are hard-coded here to alleviate the server load.
	return array(
		"FIRST SEMESTER",
		"SECOND SEMESTER",
		"FIRST SUMMER SCHOOL",
		"SECOND SUMMER SCHOOL"
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
	$xml->registerXPathNamespace('tt', 'http://upei.ca/xsd/timetable');

	// parse departments
	$department_array = array("false");
	foreach ($departments as $key => $value) {
		if ($value) {
			$department_array[] = "tt:department=\"$key\"";
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
			$semester_array[] = "tt:semester=\"$key\"";
		}
	}
	
	$semester_predicate = "";
	if (!empty($semester_array)) {
		$semester_predicate = join(" or ", $semester_array);
	}
	
	// var_dump($semester_predicate);

	$list = $xml->xpath("//tt:course[($department_predicate) and ($semester_predicate)]");

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
				'title' => strval($course->title),
				'time' => strval($course->time),
				);
		}
	}
	
	return $courses;
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