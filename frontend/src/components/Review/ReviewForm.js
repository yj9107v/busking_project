import React, {useState} from "react";
import api from "../../api/axios";

const ReviewForm = ({postId, onAddReview}) => {
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newReview = {
            postId,
            userId: 1, // TODO: 로그인 된 사용자 정보로 교체
            rating,
            comment,
        };

        try {
            const res = await api.post('/reviews', newReview);
            alert('리뷰가 등록되었습니다.');
            onAddReview(res.data);
            setRating(5);
            setComment('');
        } catch (err) {
            console.error('리뷰 등록 실패:', err);
            alert('리뷰 등록에 실패했습니다.');
        }
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
                    rows={3}
                    cols={50}
                    placeholder="리뷰를 작성해주세요"
                    required
                />
            </div>
            <button type="submit">등록</button>
        </form>
    );
};

export default ReviewForm;