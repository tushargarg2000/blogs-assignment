package com.driver.services;

import com.driver.models.Blog;
import com.driver.models.Image;
import com.driver.models.User;
import com.driver.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository1;

    @Autowired
    ImageService imageService1;

    public ArrayList<Blog> showBlogs(){
        //find all blogs
        return blogRepository1.findAll();
    }

    public Blog createAndReturnBlog(User user, String title, String content) throws ParseException {
        //create a blog at the current time
        Blog blog = new Blog();
        blog.setUser(user);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setPubDate(new Date());
        blogRepository1.save(blog);
        return blog;
    }

    public Blog findBlogById(int blogId){
        //find a blog
        return blogRepository1.findById(blogId);
    }

    public void addImage(Blog blog, String description, String dimensions){
        //add an image to the blog
        Image image = imageService1.createAndReturn(blog, description, dimensions);
        ArrayList<Image> imageList = (ArrayList<Image>) blog.getImageList();
        imageList.add(image);
    }

    public void deleteImage(Blog blog, int imageId){
        //delete a particular image
        ArrayList<Image> imageList = blog.getImageList();
        for(Image image: imageList){
            if(image.getId() == imageId){
                imageService1.deleteImage(image);
            }
        }
    }

    public void deleteBlog(int blogId){
        //delete blog and corresponding images
        Blog blog = blogRepository1.findById(blogId);
        if(blog != null){
            blogRepository1.delete(blog);
            ArrayList<Image> imageList = blog.getImageList();
            for(Image image: imageList){
                imageService1.deleteImage(image);
            }
        }
    }
}
