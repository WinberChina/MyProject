package com.mx.winber.service;

import org.springframework.stereotype.Service;

import com.mx.winber.model.Course;

@Service
public interface CourseService {
	Course getCourseById(Integer id);
}
