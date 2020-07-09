package com.tuyrk.dao;

import com.tuyrk.entity.HomepageCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ORM增删改查dao
 *
 * @author tuyrk
 */
public interface HomepageCourseDao extends JpaRepository<HomepageCourse, Long> {
}
