# react-native-hms-map

## Contents
- Introduction
- Installation Guide
- React-Native SDK Method Definition
- Configuration Description
- Licensing and Terms

## 1. Introduction

The React-Native SDK code encapsulates the Huawei location client interface. It provides many APIs for your reference or use.
The React-Native SDK code package is described as follows:

- Android: core layer, bridging Map SDK bottom-layer code;
- src/modules: Android interface layer.
- src/index.js: external interface definition layer, which is used to define interface classes or reference files.


## 2. Installation Guide
Before using Reactive-Native SDK code, ensure that the RN development environment has been installed.

### 2.1 Copy the library into the demo project

In order to able the library to be used in the demo, the library should be copied under the node_modules folder of the project.

The structure should be like this
```
        react-native-hms-map-demo
            |_ node_modules
                |_ ...
                    react-native-hms-map
                    ...
```
### 2.2 Add map package to your application

```
import com.huawei.hms.rn.map.MapsPackage;
```
Then add the following line to your getPackages() method.
```
 packages.add(new MapsPackage());
```



This will install the dependent packages used by the projects.

## 3. HUAWEI Map Kit SDK for React Native

### Components` Props
 - MapView
  
  This component using for creating map with several options that are Camera settings, Region settings and Location settings.
 - Marker
  
This component using for creating marker with several options that are Coordinate settings, Color settings and custom image settings.
 - Callout

This component using for creating callouts with several options that are custom react elements.

 - Polyline

This component using for creating polyline with several options that are Coordinates settings, Color settings and Polyline`s stroke settings.
 - Polygon

This component using for creating polyline with several options that are Coordinates settings, Color settings and Polygons`s stroke settings.
 - Circle

This component using for creating circle with several options that are CenterCoordinates settings, Color settings and Circle`s stroke settings.
 - Overlay

This component using for creating overlays with several options that are remoteURI settings of image and coordinates of this overlay image.

You can get more information from developer.huawei.com

## 4. Configure parameters.   
No.

## 5. Licensing and Terms  

>  Copyright (c) 2017 Airbnb
> 
>  Licensed under the The MIT License (MIT) (the "License");
>  you may not use this file except in compliance with the License.
>  You may obtain a copy of the License at
> 
>     https://raw.githubusercontent.com/airbnb/react-native-maps/master/LICENSE
> 
>  Unless required by applicable law or agreed to in writing, software
>  distributed under the License is distributed on an "AS IS" BASIS,
>  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>  See the License for the specific language governing permissions and
>  limitations under the License.
