package com.tuyrk.service.impl;

import com.tuyrk.client.CourseClient;
import com.tuyrk.dao.HomepageUserCourseDao;
import com.tuyrk.dao.HomepageUserDao;
import com.tuyrk.entity.HomepageUser;
import com.tuyrk.entity.HomepageUserCourse;
import com.tuyrk.service.IUserService;
import com.tuyrk.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户相关实现
 *
 * @author tuyrk
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private final HomepageUserDao homepageUserDao;
    private final HomepageUserCourseDao homepageUserCourseDao;
    private final CourseClient courseClient;

    public UserServiceImpl(HomepageUserDao homepageUserDao, HomepageUserCourseDao homepageUserCourseDao, CourseClient courseClient) {
        this.homepageUserDao = homepageUserDao;
        this.homepageUserCourseDao = homepageUserCourseDao;
        this.courseClient = courseClient;
    }

    /**
     * 创建用户
     *
     * @param request 用户信息
     * @return 用户信息
     */
    @Override
    public UserInfo createUser(CreateUserRequest request) {
        if (!request.validate()) {
            return UserInfo.invalid();
        }
        HomepageUser oldUser = homepageUserDao.findByUsername(request.getUsername());
        if (!Objects.isNull(oldUser)) {
            return UserInfo.invalid();
        }
        HomepageUser newUser = homepageUserDao.save(new HomepageUser(request.getUsername(), request.getEmail()));
        return new UserInfo(newUser.getId(), newUser.getUsername(), newUser.getEmail());
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public UserInfo getUserInfo(Long id) {
        Optional<HomepageUser> user = homepageUserDao.findById(id);
        HomepageUser curUser = user.orElse(HomepageUser.invalid());
        return new UserInfo(curUser.getId(), curUser.getUsername(), curUser.getEmail());
    }

    /**
     * 获取用户和课程信息
     *
     * @param id 用户id
     * @return 用户可课程信息
     */
    @Override
    public UserCourseInfo getUserCourseInfo(Long id) {
        Optional<HomepageUser> user = homepageUserDao.findById(id);
        HomepageUser curUser = user.orElse(null);
        if (Objects.isNull(curUser)) {
            return UserCourseInfo.invalid();
        }
        UserInfo userInfo = new UserInfo(curUser.getId(), curUser.getUsername(), curUser.getEmail());
        List<HomepageUserCourse> userCourses = homepageUserCourseDao.findAllByUserId(curUser.getId());
        if (CollectionUtils.isEmpty(userCourses)) {
            return new UserCourseInfo(userInfo, Collections.emptyList());
        }
        List<Long> courseIds = userCourses.stream().map(HomepageUserCourse::getCourseId).collect(Collectors.toList());
        List<CourseInfo> courseInfos = courseClient.getCourseInfos(new CourseInfosRequest(courseIds));
        return new UserCourseInfo(userInfo, courseInfos);
    }
}
