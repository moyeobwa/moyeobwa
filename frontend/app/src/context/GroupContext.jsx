import React, { createContext, useContext, useState } from "react";

const GroupContext = createContext();

export const useGroupContext = () => {
    return useContext(GroupContext);
}

export const GroupProvider = ({ children }) => {
    const [groups, setGroups] = useState([]);
    
    const addGroup = (newGroup) => {
        setGroups([...groups, newGroup]);
    };

    return (
        <GroupContext.Provider value={{ groups, addGroup }}>
            {children}
        </GroupContext.Provider>
    )
}

