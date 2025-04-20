import React, {useState} from "react";

const PromotionCreatePage = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [category, setCategory] = useState('MUSIC');
    const [mediaUrl, setMediaUrl] = useState('');
    const [place, setPlace] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // 백엔드 연동 전, 콘솔에 확인
        console.log({title, category, content, place, mediaUrl});
        alert("게시글이 등록되었습니다 (백엔드 연동 전)");
    };

    return(
        <div style={{padding: '20px'}}>
            <h2>📝 버스커 홍보글 등록</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>제목</label><br />
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <br />
                <div>
                    <label>카테고리</label><br />
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                    >
                        <option value="ART">예술</option>
                        <option value="DANCE">댄스</option>
                        <option value="MUSIC">음악</option>
                        <option value="TALK">토크</option>
                    </select>    
                </div>
                <br />
                <div>
                    <label>내용</label><br />
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                        rows="30"
                        cols="150"
                    />
                </div>
                <br />
                <div>
                    <label>장소</label><br />
                    <input
                        type="text"
                        value={place}
                        onChange={(e) => setPlace(e.target.value)}
                        placeholder="도로명주소를 입력하세요"
                        required
                    />
                </div>
                <div>
                    <label>사진/영상 URL</label><br />
                    <input
                        type="text"
                        value={mediaUrl}
                        onChange={(e) => setMediaUrl(e.target.value)}
                        placeholder="ex) https://youtube.com/..."
                    />
                </div>
                <br />
                <button type="submit">등록</button>
            </form>
        </div>
    );
};

export default PromotionCreatePage;
