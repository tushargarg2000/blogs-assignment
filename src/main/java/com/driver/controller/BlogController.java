package com.driver.controller;

import com.driver.models.*;
import com.driver.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.showBlogs();
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody User user,
                                           @RequestParam String title,
                                           @RequestParam String content) throws ParseException {
        Blog blog = blogService.createAndReturnBlog(user, title, content);
        return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }

    @PutMapping("/{blogId}/add-image")
    public ResponseEntity<Blog> addImage(@PathVariable int blogId, @RequestBody Image image) {
        Blog blog = blogService.findBlogById(blogId);
        if (blog != null) {
            blogService.addImage(blog, image.getDescription(), image.getDimensions());
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{blogId}/delete-image/{imageId}")
    public ResponseEntity<Blog> deleteImage(@PathVariable int blogId, @PathVariable int imageId) {
        Blog blog = blogService.findBlogById(blogId);
        if (blog != null) {
            blogService.deleteImage(blog, imageId);
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable int blogId) {
        blogService.deleteBlog(blogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}




