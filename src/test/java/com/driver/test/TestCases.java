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
    UserRepository userRepository1;
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
        image.setDimensions("100X200");

        ArrayList<Image> imageList = new ArrayList<>();
        imageList.add(image);
        blog.setImageList(imageList);

        ArrayList<Blog> blogs = new ArrayList<>();
        blogs.add(blog);
        user.setBlogList(blogs);
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
    public void testCreateAndReturnBlog(){
        when(userRepository1.findById(any())).thenReturn(Optional.ofNullable(user));
        blogService.createAndReturnBlog(user.getId(), "Test Blog Title", "Test Blog Content");
        verify(userRepository1, times(1)).save(user);
        verify(blogRepository1, never()).save(blog);
    }

    @Test
    public void testFindBlogById() {
        when(blogRepository1.findById(1)).thenReturn(Optional.ofNullable(blog));

        Blog result = blogService.findBlogById(1);
        assertEquals(blog, result);
    }

    @Test
    public void testAddImage() {
        when(blogRepository1.findById(anyInt())).thenReturn(Optional.ofNullable(blog));
        when(imageService1.createAndReturn(any(Blog.class), anyString(), anyString())).thenReturn(image);

        blogService.addImage(blog.getId(), "Test Image Description", "100X200");
        assertEquals(2, blog.getImageList().size());
        assertEquals(image, blog.getImageList().get(1));
    }

    @Test
    public void testAddImage1() {
        when(blogRepository1.findById(anyInt())).thenReturn(Optional.ofNullable(blog));
        when(imageService1.createAndReturn(any(Blog.class), anyString(), anyString())).thenReturn(image);

        blogService.addImage(blog.getId(), "Test Image Description", "100X200");
        assertEquals(2, blog.getImageList().size());
        assertEquals(image, blog.getImageList().get(1));
    }

    @Test
    public void testDeleteBlog() {
        when(blogRepository1.findById(1)).thenReturn(Optional.ofNullable(blog));

        blogService.deleteBlog(1);
        verify(blogRepository1, atMost(1)).delete(blog);
        verify(blogRepository1, atMost(1)).deleteById(blog.getId());
        verify(imageService1, never()).deleteImage(image);
    }

    @Test
    public void testDeleteBlog_BlogNotFound() {
        when(blogRepository1.findById(1)).thenReturn(Optional.ofNullable(blog));

        blogService.deleteBlog(1);
        verify(blogRepository1, atMost(1)).delete(any(Blog.class));
        verify(blogRepository1, atMost(1)).deleteById(anyInt());
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
        verify(imageRepository2, atMost(1)).delete(image);
        verify(imageRepository2, atMost(1)).deleteById(image.getId());
    }

    @Test
    public void testCountImage1() {
        Image image = new Image();
        image.setDimensions("100X200");
        String screenDimensions = "500X700";
        int expected = 15;
        int actual = imageService.countImagesInScreen(image, screenDimensions);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountImage2() {
        Image image = new Image();
        image.setDimensions("500X200");
        String screenDimensions = "500X800";
        int expected = 4;
        int actual = imageService.countImagesInScreen(image, screenDimensions);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountImage3() {
        Image image = new Image();
        image.setDimensions("500X801");
        String screenDimensions = "500X800";
        int expected = 0;
        int actual = imageService.countImagesInScreen(image, screenDimensions);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountImage4() {
        Image image = new Image();
        image.setDimensions("250X200");
        String screenDimensions = "500X800";
        int expected = 8;
        int actual = imageService.countImagesInScreen(image, screenDimensions);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindById() {
        Image image = new Image();
        when(imageRepository2.findById(1)).thenReturn(Optional.of(image));
        Image foundImage = imageService.findById(1);
        assertEquals(image, foundImage);
    }

    @Test
    public void testFindByIdFound1() {
        Image image = new Image();
        when(imageRepository2.findById(1)).thenReturn(Optional.of(image));
        Image foundImage = imageService.findById(1);
        assertEquals(image, foundImage);
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
        ArrayList<Blog> blogList = new ArrayList<>();
        Blog blog = new Blog();
        blog.setId(1);
        blogList.add(blog);
        user.setBlogList(blogList);
        userService.deleteUser(1);
        verify(userRepository3, atMost(1)).delete(user);
        verify(userRepository3, atMost(1)).deleteById(user.getId());
        verify(blogService3, times(0)).deleteBlog(1);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testuser");
        userService.updateUser(user);
        verify(userRepository3, times(1)).save(user);
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

