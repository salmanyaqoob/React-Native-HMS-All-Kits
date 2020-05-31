/*
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

import {StyleSheet} from "react-native";
import {Colors} from "react-native/Libraries/NewAppScreen";

export const styles = StyleSheet.create({
  body: {
    backgroundColor: Colors.white,
    paddingHorizontal: 30,
  },
  container: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    justifyContent: "flex-end",
    alignItems: "center",
  },
  holder: {
    alignItems: "center",
    justifyContent: "center",
    position: "absolute",
    width: 300,
    height: 40,
    backgroundColor: "white",
    zIndex: 2,
    shadowColor: "#000",
    shadowOffset: {width: 2, height: 0},
    shadowOpacity: 1,
    shadowRadius: 15,
    elevation: 7,
  },
  picker: {
    position: "absolute",
    top: 0,
    left: 0,
    height: 50,
    width: 300,
    color: "#000000",
  },

  sectionContainer: {
    paddingVertical: 10,
    backgroundColor: "#e2f5f9",
    borderRadius: 10,
    marginTop: 10,
  },
  sectionHeading: {
    color: "black",
    fontSize: 16,
    textAlign: "center",
    fontWeight: "bold",
    paddingBottom: 10,
  },

  container10Top: {
    flexDirection: "row",
    justifyContent: "space-around",
    marginBottom: 5,
  },

  containerShowResultMsg: {
    flexDirection: "row",
    // marginTop: 10,
  },

  containerSeamless: {
    flexDirection: "row",
    justifyContent: "space-around",
    // backgroundColor: "#2196F3",
  },

  containerTopicInput: {
    flexDirection: "row",
    backgroundColor: "#2196F3",
    marginTop: 10,
    flex: 1,
  },
  inputTopic: {
    textAlign: "center",
    marginHorizontal: 15,
    color: "white",
    width: "92%",
    backgroundColor: "#58abaa",
  },

  buttonCenter: {
    marginBottom: 2,
    width: 250,
    // height: 40,
    alignItems: "center",
    backgroundColor: "#2196F3",
    justifyContent: "center",
    textAlign: "center",
  },

  buttonLeft: {
    marginBottom: 2,
    width: "auto",
    // height: 40,
    alignItems: "center",
    backgroundColor: "#2196F3",
  },
  buttonRight: {
    flexWrap: "nowrap",
    // height: 40,
    alignItems: "flex-start",
    backgroundColor: "#2196F3",
  },

  buttonTextSmall: {
    textAlign: "center",
    padding: 4,
    color: "white",
    // width: 200,
    // minWidth: 140,
    height: 26,
    alignItems: "center",
    textTransform: "none",
  },

  buttonText: {
    textAlign: "center",
    padding: 10,
    color: "white",
    // width: 200,
    minWidth: 140,
    height: 40,
    alignItems: "center",
    textTransform: "none",
  },
  imageView: {
    flex: 1,
    backgroundColor: "#fff",
    flexDirection: "column",
  },
  image: {
    justifyContent: "flex-start",
    width: 400,
    height: 80,
  },
  paddingX: {
    paddingTop: 75,
  },
});
