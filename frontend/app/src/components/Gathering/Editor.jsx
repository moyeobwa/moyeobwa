import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useGroupContext } from "../../context/GroupContext";
import Button from "../Button";
import "./Editor.css";

const Editor = () => {
    const { addGroup } = useGroupContext();
    const [category, setCategory] = useState("");
    const [groupName, setGroupName] = useState("");
    const [description, setDescription] = useState("");
    const [hashTags, setHashTags] = useState([]);
    const [tagInput, setTagInput] = useState("");
    const [capacity, setCapacity] = useState("");
    const [image, setImage] = useState(null);

    const token = localStorage.getItem('token');
    const apiUrl = import.meta.env.VITE_API_BASE_URL;

    const nav = useNavigate();

    const onChangeCategory = (e) => {
        setCategory(e.target.value);
    };

    const onChangeGroupName = (e) => {
        setGroupName(e.target.value);
    };

    const onChangeDescription = (e) => {
        setDescription(e.target.value);
    };

    const onChangeCapacity = (e) => {
        setCapacity(e.target.value);
    };

    const onImageChange = (e) => {
        setImage(e.target.files[0]);
    };

    const onTagInputChange = (e) => {
        setTagInput(e.target.value);
    };

    const onTagInputKeyPress = (e) => {
        if (e.key === "Enter" && tagInput.trim() !== "") {
            setHashTags([...hashTags, tagInput.trim()]);
            setTagInput("");
        }
    };

    const onAddTagClick = () => {
        if (tagInput.trim() !== "") {
            setHashTags([...hashTags, tagInput.trim()]);
            setTagInput("");
        }
    };

    const onRemoveTagClick = (tagToRemove) => {
        setHashTags(hashTags.filter(tag => tag !== tagToRemove));
    };

    const onSubmitButtonClick = async () => {
        const formData = new FormData();
        formData.append("image", image);
        formData.append("request", new Blob([JSON.stringify({
            category,
            name: groupName,
            description,
            tagNames: hashTags.length > 0 ? hashTags : []
        })], { type: "application/json" }));

        try {
            const response = await fetch(`${apiUrl}/api/v1/gatherings`, {
                method: "POST",
                body: formData,
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });


            if (response.ok) {
                const location = response.headers.get("Location");
                if (location) {
                    const newGroupId = location.split("/").pop();
                    addGroup({
                        id: newGroupId,
                        category,
                        groupName,
                        description,
                        hashTags,
                        capacity,
                    });
                    nav('/', { replace: true });
                } else {
                    console.error("Location header is missing in the response");
                }
            } else {
                console.error("Failed to create gathering");
            }
        } catch (error) {
            console.error("Error:", error);
        }
    };

    return (
        <div className="Editor">
            <div className="section_wrapper">
                <section className="category_section">
                    <h4>카테고리</h4>
                    <div className="input_wrapper">
                        <select value={category} onChange={onChangeCategory}>
                            <option value="" disabled>카테고리를 선택해주세요</option>
                            <option value="EXERCISE">운동</option>
                            <option value="STUDY">공부</option>
                        </select>
                    </div>
                </section>
                <section className="name_section">
                    <h4>그룹 이름</h4>
                    <div className="input_wrapper">
                        <input
                            name="groupName"
                            onChange={onChangeGroupName}
                            value={groupName}
                            placeholder="그룹명을 입력해주세요."
                        />
                    </div>
                </section>
            </div>
            <div className="section_wrapper">
                <section className="capacity_section">
                    <h4>제한 인원수</h4>
                    <div className="input_wrapper">
                        <input
                            name="capacity"
                            onChange={onChangeCapacity}
                            value={capacity}
                            placeholder="제한 인원수를 입력하세요."
                        />
                    </div>
                </section>
                <section className="hashTag_section">
                    <h4>해시 태그</h4>
                    <div className="input_wrapper">
                        <input
                            name="tagInput"
                            value={tagInput}
                            onChange={onTagInputChange}
                            onKeyPress={onTagInputKeyPress}
                            placeholder="해시 태그를 입력하고 Enter를 누르세요"
                        />
                        <button type="button" onClick={onAddTagClick}>추가</button>
                    </div>
                    <div className="tag_list">
                        {hashTags.map((tag, index) => (
                            <div key={index} className="tag_item">
                                {tag}
                                <button type="button" onClick={() => onRemoveTagClick(tag)}>x</button>
                            </div>
                        ))}
                    </div>
                </section>
            </div>
            <section className="description_section">
                <h4 className="description_title">그룹 소개</h4>
                <div className="input_wrapper">
                    <textarea
                        name="description"
                        value={description}
                        onChange={onChangeDescription}
                        placeholder="그룹을 소개해주세요!"
                    />
                </div>
            </section>
            <section className="image_section">
                <h4>이미지 업로드</h4>
                <div className="input_wrapper">
                    <input
                        type="file"
                        onChange={onImageChange}
                    />
                </div>
            </section>
            <section className="button_section">
                <Button onClick={() => nav(-1)} text={"취소하기"} />
                <Button onClick={onSubmitButtonClick} text={"그룹 생성"} />
            </section>
        </div>
    );
};

export default Editor;
