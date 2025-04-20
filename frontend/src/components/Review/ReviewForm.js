import React, {useState} from "react";

const ReviewForm = ({postId, onAddReview}) => {
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        const newReview = {
            id: Date.now(),
            postId,
            userId: 999, // 현재는 mock user
            rating,
            comment,
            createdAt: new Date().toISOString(),
        };

        onAddReview(newReview);
        setRating(5);
        setComment('');
    };

    return(
        <form onSubmit={handleSubmit} style={{marginTop: '20px'}}>
            <h4>리뷰 작성</h4>
            <div>
                <label>별점 : </label>
                <select value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                    {[1, 2, 3, 4, 5].map((val) => (
                        <option key={val} value={val}>
                            {val}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <textarea
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    rows={4}
                    cols={50}
                    placeholder="리뷰를 입력하세요"
                    required
                />
            </div>
            <button type="submit">등록</button>
        </form>
    );
};

export default ReviewForm;