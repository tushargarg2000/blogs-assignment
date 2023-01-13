package com.driver.services;

import com.driver.models.Blog;
import com.driver.models.Image;
import com.driver.models.User;
import com.driver.repositories.BlogRepository;
import com.driver.repositories.UserRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository1;

    @Autowired
    ImageService imageService1;

    @Autowired
    UserRepository userRepository;

    public List<Blog> showBlogs(){
        //find all blogs
        return blogRepository1.findAll();
    }

    public Blog createAndReturnBlog(Integer userId, String title, String content) throws ParseException {
        //create a blog at the current time
        Blog blog = new Blog();
        User user = userRepository.findById(userId).get();

        //updating the blog details
        blog.setUser(user);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setPubDate(new Date());


        //Updating the userInformation and changing its blogs
        List<Blog> curentBlogs = user.getBlogList();
        curentBlogs.add(blog);
        user.setBlogList(curentBlogs);

        //Only calling the parent userRepository function as the child function will automatically be called by cascading
        userRepository.save(user);

        return blog;
    }

    public Blog findBlogById(int blogId){
        //find a blog
        return blogRepository1.findById(blogId).get();
    }

    public void addImage(Integer blogId, String description, String dimensions){
        //add an image to the blog
        Blog blog = blogRepository1.findById(blogId).get();

        Image image = imageService1.createAndReturn(blog, description, dimensions);
        List<Image> imageList = blog.getImageList();
        imageList.add(image);
        blog.setImageList(imageList);

        blogRepository1.save(blog); //Just calling the parent repository and child repository will automatically be called.
    }

    public void deleteImage(Blog blog, int imageId){
        //delete a particular image
        List<Image> imageList = blog.getImageList();
        for(Image image: imageList){
            if(image.getId() == imageId){
                imageService1.deleteImage(image);
            }
        }
    }

    public void deleteBlog(int blogId){
        //delete blog and corresponding images
        Blog blog = blogRepository1.findById(blogId).get();
        if(blog != null){
            blogRepository1.delete(blog);
            //This piece of code is not required to write: it will be automatically taken care of.
//            List<Image> imageList = blog.getImageList();
//            for(Image image: imageList){
//                imageService1.deleteImage(image);
//            }
        }
    }
}
