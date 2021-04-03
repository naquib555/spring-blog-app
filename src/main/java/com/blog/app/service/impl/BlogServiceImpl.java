package com.blog.app.service.impl;

import com.blog.app.common.enums.ResponseCode;
import com.blog.app.common.exception.ResourceNotFoundException;
import com.blog.app.common.util.Utils;
import com.blog.app.enums.Rating;
import com.blog.app.enums.RoleName;
import com.blog.app.model.entities.BlogCommentsEntity;
import com.blog.app.model.entities.BlogEntity;
import com.blog.app.model.entities.BlogRatingEntity;
import com.blog.app.model.entities.UserEntity;
import com.blog.app.model.mapper.BlogMapper;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.model.payload.blog.BlogDetailsResponse;
import com.blog.app.model.payload.blog.CommentRequest;
import com.blog.app.model.payload.blog.PostBlogRequest;
import com.blog.app.model.payload.blog.PostBlogResponse;
import com.blog.app.repository.BlogCommentsRepository;
import com.blog.app.repository.BlogRatingRepository;
import com.blog.app.repository.BlogRepository;
import com.blog.app.security.domain.UserPrincipal;
import com.blog.app.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogCommentsRepository blogCommentsRepository;

    @Autowired
    private BlogRatingRepository blogRatingRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Transactional
    @Override
    public ApiResponse postBlog(PostBlogRequest postBlogRequest, Long userId) {
        BlogEntity blogEntity = new BlogEntity(postBlogRequest, userDetailsService.getUserById(userId));
        blogEntity = blogRepository.save(blogEntity);
        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Blog created", new PostBlogResponse(blogEntity.getBlogId()));
    }

    @Transactional
    @Override
    public ApiResponse sendForApproval(Long blogId, Long userId) {
        BlogEntity blogEntity = getBlogEntity(blogId);

        if(blogEntity.getUser().getUserId() != userId)
            return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), "Not authorized to sent approval for this blog");


        if(Utils.NVL(blogEntity.getReadyForApproval(), false))
            return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), "Pending for admin approval");

        blogEntity.setReadyForApproval(true);
        blogRepository.save(blogEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Sent for approval");
    }

    @Transactional
    @Override
    public ApiResponse approveBlog(Long blogId, Long userId) {
        BlogEntity blogEntity = getBlogEntity(blogId);


        //Optional<BlogApprovedEntity> optionalBlogApproved = blogApprovedRepository.findById(blogEntity);

        if(Utils.NVL(blogEntity.getApproved(), false))
            return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), "This blog is already approved");

        if(!Utils.NVL(blogEntity.getReadyForApproval(), false))
            return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), "This blog is not ready for approval");

        blogEntity.setApproved(true);
        blogEntity.setApprovedBy(userDetailsService.getUserById(userId));
        blogRepository.save(blogEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Blog Approved");
    }

    @Override
    public ApiResponse deletePost(Long blogId, UserPrincipal userPrincipal) {
        BlogEntity blogEntity = getBlogEntity(blogId);

        if(userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.USER.name()))) {
            if(blogEntity.getUser().getUserId() != userPrincipal.getUserId())
                return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), "Not authorized to delete this blog");
        }


        blogEntity.setActive(false);
        blogRepository.save(blogEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Blog Deleted.");
    }

    @Override
    public ApiResponse listAllBlogs(Integer searchLimit) {
        BlogEntity parameter = new BlogEntity();
        parameter.setActive(true);
        parameter.setApproved(true);


        Example<BlogEntity> example = Example.of(parameter);

//        Sort sortBy = isOrderByAsc ? Sort.by("createdDate").ascending() : Sort.by("createdDate").descending();

//        Pageable pageable = !Utils.isEmpty(searchLimit) ? PageRequest.of(0, searchLimit.intValue(), sortBy) : PageRequest.of(0, Integer.MAX_VALUE, sortBy);
        Pageable pageable = !Utils.isEmpty(searchLimit) ? PageRequest.of(0, searchLimit.intValue()) : PageRequest.of(0, Integer.MAX_VALUE);

        Page<BlogEntity> pageEntities = blogRepository.findAll(example, pageable);
//        Page<BlogApprovedEntity> pageEntities = blogApprovedRepository.findAll(example, pageable);

        Page<BlogMapper> blogs = pageEntities.map(new Function<BlogEntity, BlogMapper>() {
            @Override
            public BlogMapper apply(BlogEntity blogEntity) {
                return new BlogMapper(blogEntity);
            }
        });

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Success", blogs);
    }

    @Override
    public ApiResponse getBlog(Long blogId) {
        BlogEntity blogEntity = getBlogEntity(blogId);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Success", new BlogDetailsResponse(blogEntity));
    }

    @Override
    public ApiResponse likeBlog(Long blogId, Rating rating, Long userId) {
        BlogEntity blogEntity = getBlogEntity(blogId);
        UserEntity userEntity = userDetailsService.getUserById(userId);

        Optional<BlogRatingEntity> optionalBlogRatingEntity = blogRatingRepository.findByBlogAndRatedBy(blogEntity, userEntity);
        BlogRatingEntity blogRatingEntity;

        if(optionalBlogRatingEntity.isPresent()) {
            blogRatingEntity = optionalBlogRatingEntity.get();
            if(blogRatingEntity.getRating() == rating)
                return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), "Already Rated this blog with " + rating.name());
        }
        else blogRatingEntity = new BlogRatingEntity(rating, blogEntity, userEntity);

        blogRatingRepository.save(blogRatingEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Rating Added");
    }

    @Override
    public ApiResponse commentBlog(CommentRequest commentRequest, Long userId) {
        BlogEntity blogEntity = getBlogEntity(commentRequest.getBlogId());
        UserEntity userEntity = userDetailsService.getUserById(userId);

        BlogCommentsEntity blogCommentsEntity = new BlogCommentsEntity(commentRequest, blogEntity, userEntity);

        blogCommentsRepository.save(blogCommentsEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Comment Added");
    }

    public BlogEntity getBlogEntity(Long blogId) {
        BlogEntity parameter = new BlogEntity();
        parameter.setBlogId(blogId);
        parameter.setActive(true);
        Example<BlogEntity> example = Example.of(parameter);

        return blogRepository
                .findOne(example)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "blogId", blogId));
    }
}
