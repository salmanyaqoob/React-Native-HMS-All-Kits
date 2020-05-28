import React, {useContext, useState} from 'react';
import {NativeModules} from 'react-native';

const Config = React.createContext({
  isHmsAvailable: '',
  isGMSAvailable: '',
});

export const useConfig = () => useContext(Config);

export const AppContext = ({children}) => {
  const [isHmsAvailable, setIsHmsAvailable] = useState(false);
  const [isGMSAvailable, setIsGMSAvailable] = useState(false);

  const checkHmsAvailability = () => {
    NativeModules.HMSBase.isHmsAvailable(availability => {
      setIsHmsAvailable(availability);
    });
  };

  const checkGmsAvailability = () => {
    NativeModules.HMSBase.isGmsAvailable(availability => {
      setIsGMSAvailable(availability);
    });
  };

  checkHmsAvailability();
  checkGmsAvailability();

  const context = {isHmsAvailable, isGMSAvailable};

  return <Config.Provider value={context}>{children}</Config.Provider>;
};
