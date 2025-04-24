import React, {useState, useEffect} from 'react';
import ReviewItem from './ReviewItem';
import ReviewForm from './ReviewForm';
import api from '../../api/axios';

const ReviewList = ({postId, onReviewsLoaded}) => {
    const [reviews, setReviews] = useState([]);

    // 리뷰 목록 가져오기
    useEffect(() => {
        api.get(`/reviews?postId=${postId}`)
            .then(res => {
                setReviews(res.data);
                onReviewsLoaded?.(res.data);
            })
            .catch(err => {
                console.error("리뷰 목록 불러오기 실패:", err);
            });
    }, [postId, onReviewsLoaded]);

    const handleAddReview = (newReview) => {
        setReviews(prev => [...prev, newReview]);
        onReviewsLoaded?.([...reviews, newReview]);
    };

    const handleDeleteReview = (reviewId) => {
        const updated = reviews.filter(r => r.id !== reviewId);
        setReviews(updated);
        onReviewsLoaded?.(updated);
    };

    const handleUpdateReview = (updatedReview) => {
        const updated = reviews.map((r) =>
            r.id === updatedReview.id ? updatedReview : r
        );
        setReviews(updated);
        onReviewsLoaded?.(updated);
    };

    return(
        <div>
            <ReviewForm postId={postId} onAddReview={handleAddReview} />
            <br />
            ===================================================================================
            {reviews.map((review) => (
                <ReviewItem
                    key={review.id}
                    review={review}
                    onDelete={handleDeleteReview}
                    onUpdate={handleUpdateReview}
                />
            ))}
        </div>
    );
};

export default ReviewList;