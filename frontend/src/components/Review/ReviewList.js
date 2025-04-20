import React, {useState, useEffect} from 'react';
import ReviewItem from './ReviewItem';
import ReviewForm from './ReviewForm';

const ReviewList = ({postId, onReviewsLoaded}) => {
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        // 처음 렌더링 시 mock 리뷰 데이터 로드
        const mockReviews = [
            {
                id: 1,
                postId,
                userId: 1,
                rating: 5,
                comment: '최고의 공연이었어요!',
                createdAt: new Date().toISOString(),
            },
            {
                id: 2,
                postId,
                userId: 2,
                rating: 4,
                comment: '즐거웠습니다!',
                createdAt: new Date().toISOString(),
            }
        ];
        setReviews(mockReviews);
        onReviewsLoaded?.(mockReviews); // 부모에게 전달
    }, [postId, onReviewsLoaded]);

    const handleAddReview = (newReview) => {
        const updated = [...reviews, newReview];
        setReviews(updated);
        onReviewsLoaded?.(updated);
    };

    const handleDeleteReview = (reviewId) => {
        const updated = reviews.filter((r) => r.id !== reviewId);
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