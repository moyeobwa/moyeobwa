import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useGroupContext } from "../../context/GroupContext";
import Button from "../Button";
import "./Editor.css";

const Editor = () => {
    const { addGroup } = useGroupContext();
    const [category, setCategory] = useState("");
    const [groupName, setGroupName] = useState("");
    const [description, setDescription] = useState("");
    const [hashTags, setHashTags] = useState("");
    const [capacity, setCapacity] = useState("");


    const nav = useNavigate();

    const onChangeCategory = (e) => {
        setCategory(e.target.value)
    };

    const onChangeGroupName = (e) => {
        setGroupName(e.target.value);
    };

    const onChangeDescription = (e) => {
        setDescription(e.target.value);
    };

    const onChangeHashTags = (e) => {
        setHashTags(e.target.value);
    };

    const onChangeCapacity = (e) => {
        setCapacity(e.target.value);
    }

    const onSubmitButtonClick = () => {
        addGroup({
            category,
            groupName,
            description,
            hashTags,
            capacity,
        });
        nav('/', {replace: true});
    };

    return (
        <div className="Editor">
          <div className="section_wrapper">
            <section className="category_section">
              <h4>카테고리</h4>
              <div className="input_wrapper">
                <input
                  name="category"
                  onChange={onChangeCategory}
                  value={category}
                  placeholder="카테고리를 입력해주세요."
                />
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
                  name="groupName"
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
                  name="hashTags"
                  onChange={onChangeHashTags}
                  value={hashTags}
                  placeholder="해시 태그를 입력해주세요."
                />
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
          <section className="button_section">
            <Button onClick={() => nav(-1)} text={"취소하기"} />
            <Button onClick={onSubmitButtonClick} text={"그룹 생성"} />
          </section>
        </div>
      );
}
 
export default Editor;
