import React, {useState, useEffect} from 'react';
import ReviewItem from './ReviewItem';
import ReviewForm from './ReviewForm';

const ReviewList = ({postId}) => {
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        // 처음 렌더링 시 mock 리뷰 데이터 로드
        const mockReviews = [
            {
                id: 1,
                postId,
                userId: 1,
                rating: 5,
                coment: '최고의 공연이었어요!',
                createdAt: new Date().toISOString(),
            },
        ];
        setReviews(mockReviews);
    }, [postId]);

    const handleAddReview = (newReview) => {
        setReviews((prev) => [...prev, newReview]);
    };

    const handleDeleteReview = (reviewId) => {
        setReviews((prev) => prev.filter((r) => r.id !== reviewId));
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
                />
            ))}
        </div>
    );
};

export default ReviewList;
