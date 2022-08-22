package com.example.demo.services;

import com.example.demo.model.Reviews;
import com.example.demo.repository.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;

    @Autowired
    public ReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }
    public List<Reviews> showAllReviews() {
        return reviewsRepository.findAll();
    }

    public void updateReview(Integer id,String userName, String title, String subject, String updated) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            if (!userName.isEmpty()) {
                reviews.setUserName(userName);
            }
            if (!title.isEmpty()) {
                reviews.setTitle(title);
            }
            if (!subject.isEmpty()) {
                reviews.setSubject(subject);
            }
            if (updated != null) {
                reviews.setUpdated(updated);
            }
            reviews.setStar1("blue");
            reviews.setStar2("blue");
            reviews.setStar3("blue");
            reviews.setStar4("blue");
            reviews.setStar5("blue");
            reviewsRepository.save(reviews);
        }
    }

    public void createReview(Reviews reviews,String name,String userName, String title, String subject, String updated) {
        if (reviewsRepository.findByName(name) == null) {
            reviews.setUserName(userName);
            reviews.setTitle(title);
            reviews.setSubject(subject);
            reviews.setUpdated(updated);
            reviews.setStar1("blue");
            reviews.setStar2("blue");
            reviews.setStar3("blue");
            reviews.setStar4("blue");
            reviews.setStar5("blue");
            reviewsRepository.save(reviews);
        }else {
            for(Reviews review : reviewsRepository.findAllByName(name)) {
                 updateReview(review.getId(), userName, title, subject, updated);
            }
        }
    }


    public void createName(String name) {
        Reviews reviews = new Reviews();
        reviews.setName(name);
        reviewsRepository.save(reviews);
    }


    public void updateStar1(Integer id, String star1) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            reviews.setStar1(star1);
            reviewsRepository.save(reviews);
        }
    }

    public String getStar1(Integer id) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            return reviews.getStar1();
        }
        return null;
    }

    public void updateStar2(Integer id, String star2) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            reviews.setStar2(star2);
            reviewsRepository.save(reviews);
        }
    }

    public String getStar2(Integer id) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            return reviews.getStar2();
        }
        return null;
    }

    public void updateStar3(Integer id, String star3) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            reviews.setStar3(star3);
            reviewsRepository.save(reviews);
        }
    }

    public String getStar3(Integer id) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            return reviews.getStar3();
        }
        return null;
    }

    public void updateStar4(Integer id, String star4) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            reviews.setStar4(star4);
            reviewsRepository.save(reviews);
        }
    }

    public String getStar4(Integer id) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            return reviews.getStar4();
        }
        return null;
    }

    public void updateStar5(Integer id, String star5) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            reviews.setStar5(star5);
            reviewsRepository.save(reviews);
        }
    }

    public String getStar5(Integer id) {
        Reviews reviews = reviewsRepository.findById(id).get();
        if (reviews.getId().equals(id)) {
            return reviews.getStar5();
        }
        return null;
    }


    public List<Reviews> reviewsByName(String name) {
        return reviewsRepository.findMyReviews(name);
    }

    public void deleteByName(String name) {
        Reviews reviews = reviewsRepository.findByName(name);
        if (reviews != null) {
            reviewsRepository.delete(reviews);
        }
    }
}
