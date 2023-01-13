package com.driver.test;

import com.driver.models.*;
import com.driver.repositories.*;
import com.driver.services.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCases {
    @InjectMocks
    BlogService blogService;

    @InjectMocks
    ImageService imageService;

    @InjectMocks
    UserService userService;

    @Mock
    BlogRepository blogRepository1;
    @Mock
    ImageService imageService1;
    @Mock
    ImageRepository imageRepository2;
    @Mock
    UserRepository userRepository3;
    @Mock
    BlogService blogService3;

    User user;
    Blog blog;
    Image image;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("testpassword");

        blog = new Blog();
        blog.setId(1);
        blog.setUser(user);
        blog.setTitle("Test Blog Title");
        blog.setContent("Test Blog Content");
        blog.setPubDate(new Date());

        image = new Image();
        image.setId(1);
        image.setBlog(blog);
        image.setDescription("Test Image Description");
        image.setDimensions("Test Image Dimensions");

        ArrayList<Image> imageList = new ArrayList<>();
        imageList.add(image);
        blog.setImageList(imageList);
    }

    @Test
    public void testShowBlogs() {
        ArrayList<Blog> blogs = new ArrayList<>();
        blogs.add(blog);
        when(blogRepository1.findAll()).thenReturn(blogs);

        List<Blog> result = blogService.showBlogs();
        assertEquals(blogs, result);
    }

    @Test
    public void testCreateAndReturnBlog() throws Exception {
        when(blogRepository1.save(any(Blog.class))).thenReturn(blog);

        Blog result = blogService.createAndReturnBlog(user.getId(), "Test Blog Title", "Test Blog Content");
        assertEquals(blog.getContent(), result.getContent());
        assertEquals(blog.getUser(), result.getUser());
        assertEquals(blog.getTitle(), result.getTitle());
    }

    @Test
    public void testFindBlogById() {
        when(blogRepository1.findById(1)).thenReturn(Optional.ofNullable(blog));

        Blog result = blogService.findBlogById(1);
        assertEquals(blog, result);
    }

    @Test
    public void testAddImage() {
        when(imageService1.createAndReturn(any(Blog.class), anyString(), anyString())).thenReturn(image);

        blogService.addImage(blog.getId(), "Test Image Description", "Test Image Dimensions");
        assertEquals(2, blog.getImageList().size());
        assertEquals(image, blog.getImageList().get(1));
    }

    @Test
    public void testDeleteImage1() {
        ArrayList<Image> imageList = new ArrayList<>();
        imageList.add(image);
        blog.setImageList(imageList);

        blogService.deleteImage(blog, 1);
        assertEquals(1, blog.getImageList().size());
    }

    @Test
    public void testDeleteBlog() {
        when(blogRepository1.findById(1)).thenReturn(Optional.ofNullable(blog));

        blogService.deleteBlog(1);
        verify(blogRepository1, times(1)).delete(blog);
        verify(imageService1, times(1)).deleteImage(image);
    }

    @Test
    public void testDeleteBlog_BlogNotFound() {
        when(blogRepository1.findById(1)).thenReturn(null);

        blogService.deleteBlog(1);
        verify(blogRepository1, never()).delete(any(Blog.class));
        verify(imageService1, never()).deleteImage(any(Image.class));
    }

    @Test
    public void testCreateAndReturn() {
        Blog blog = new Blog();
        Image image = imageService.createAndReturn(blog, "Test Description", "Test Dimensions");
        assertNotNull(image);
        assertEquals("Test Description", image.getDescription());
        assertEquals("Test Dimensions", image.getDimensions());
        assertEquals(blog, image.getBlog());
    }

    @Test
    public void testDeleteImage() {
        Image image = new Image();
        imageService.deleteImage(image);
        verify(imageRepository2, times(1)).delete(image);
    }

    @Test
    public void testFindById() {
        Image image = new Image();
        when(imageRepository2.findById(1)).thenReturn(image);
        Image foundImage = imageService.findById(1);
        assertEquals(image, foundImage);
    }

    @Test
    public void testFindById_NotFound() {
        when(imageRepository2.findById(1)).thenReturn(null);
        Image foundImage = imageService.findById(1);
        assertNull(foundImage);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        userService.createUser(user);
        verify(userRepository3, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        when(userRepository3.findById(1)).thenReturn(user);
        ArrayList<Blog> blogList = new ArrayList<>();
        Blog blog = new Blog();
        blog.setId(1);
        blogList.add(blog);
        user.setBlogList(blogList);
        userService.deleteUser(1);
        verify(userRepository3, times(1)).delete(user);
        verify(blogService3, times(1)).deleteBlog(1);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testuser");
        userService.updateUser(user);
        verify(userRepository3, times(1)).update(user);
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository3.findByUsername("testuser")).thenReturn(user);
        User foundUser = userService.findUserByUsername("testuser");
        assertEquals(foundUser.getUsername(), "testuser");
    }

    @Test
    public void testFindUserByUsername_userNotFound() {
        when(userRepository3.findByUsername("testuser")).thenReturn(null);
        User foundUser = userService.findUserByUsername("testuser");
        assertEquals(foundUser, null);
    }
}

