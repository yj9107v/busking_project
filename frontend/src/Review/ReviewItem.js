import React from "react";

const ReviewItem = ({ review, onDelete}) => {
    const {rating, comment, createdAt} = review;

    return(
        <div style={{border: '1px solid #ccc', padding: '10px', marginBottom: '10px'}}>
            <div>⭐ {rating}점</div>
            <div>{comment}</div>
            <div style={{fontSize: '12px', color: 'gray'}}>
                작성일: {new Date(createdAt).toLocaleString()}
            </div>
            <button onClick={() => onDelete(review.id)}>삭제</button>
        </div>
    );
};

export default ReviewItem;
