package com.lambdaschool.school.service;

import com.lambdaschool.school.exception.ResourceNotFoundException;
import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "studentService")
public class StudentServiceImpl implements StudentService
{
    @Autowired
    private StudentRepository studrepos;

    @Override
    public List<Student> findAll(Pageable pageable)
    {
        List<Student> list = new ArrayList<>();
        studrepos.findAll(pageable).iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Student findStudentById(long id) throws ResourceNotFoundException
    {
        System.out.println("Made it to service");
        Student rtnStudent = studrepos.findByStudid(id);
        
        if(rtnStudent == null)
        {
            throw new ResourceNotFoundException("Could not find student with ID " + id);
        }
        
        return rtnStudent;
        
    }

    @Override
    public List<Student> findStudentByNameLike(String name, Pageable pageable)
    {
        List<Student> list = new ArrayList<>();
        studrepos.findByStudnameContainingIgnoreCase(name, pageable).iterator().forEachRemaining(list::add);
        if(list.size() == 0)
        {
            throw new ResourceNotFoundException("No students found with name " + name);
        }
        return list;
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException
    {
        if (studrepos.findById(id).isPresent())
        {
            studrepos.deleteById(id);
        } else
        {
            throw new ResourceNotFoundException("Could not delete student, no student found with Id: " + id);
        }
    }

    @Transactional
    @Override
    public Student save(Student student)
    {
        Student newStudent = new Student();

        newStudent.setStudname(student.getStudname());
    
        System.out.println(newStudent.toString());

        return studrepos.save(newStudent);
    }

    @Override
    public Student update(Student student, long id)
    {
        Student currentStudent = studrepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));

        if (student.getStudname() != null)
        {
            currentStudent.setStudname(student.getStudname());
        }

        return studrepos.save(currentStudent);
    }
}
