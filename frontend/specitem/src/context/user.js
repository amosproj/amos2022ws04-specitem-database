import { createContext } from 'react';

const UserContext = createContext({language: "en",
setLanguage: () => {}});
export default UserContext;
