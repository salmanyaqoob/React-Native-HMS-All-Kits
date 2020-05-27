/*Copyright (c) 2015 Airbnb

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

2020.03.05-modified according to Huawei Map
Huawei Technologies Co., Ltd.
*/

import PropTypes from 'prop-types';
import {requireNativeComponent, NativeModules, Platform} from 'react-native';
import {PROVIDER_DEFAULT, PROVIDER_HUAWEI} from './ProviderConstants';

export const SUPPORTED = 'SUPPORTED';
export const USES_DEFAULT_IMPLEMENTATION = 'USES_DEFAULT_IMPLEMENTATION';
export const NOT_SUPPORTED = 'NOT_SUPPORTED';

export function getAirMapName(provider) {
  return 'AIRMap';
}

function getAirComponentName(provider, component) {
  return `${getAirMapName(provider)}${component}`;
}

export const contextTypes = {
  provider: PropTypes.string,
};

export const createNotSupportedComponent = message => () => {
  console.error(message);
  return null;
};

function getViewManagerConfig(viewManagerName) {
  const UIManager = NativeModules.UIManager;
  if (!UIManager.getViewManagerConfig) {
    // RN < 0.58
    return UIManager[viewManagerName];
  }
  // RN >= 0.58
  return UIManager.getViewManagerConfig(viewManagerName);
}

export default function decorateMapComponent(
  Component,
  {componentType},
) {
  const components = {};

  const getDefaultComponent = () =>
    requireNativeComponent(getAirComponentName(null, componentType), Component);

  Component.contextTypes = contextTypes;

  Component.prototype.getAirComponent = function getAirComponent() {
    const provider = this.context.provider || PROVIDER_DEFAULT;
    if (components[provider]) {
      return components[provider];
    }

    if (provider === PROVIDER_DEFAULT) {
      components[PROVIDER_DEFAULT] = getDefaultComponent();
      return components[PROVIDER_DEFAULT];
    }

    if (!components[PROVIDER_DEFAULT]) {
      components[PROVIDER_DEFAULT] = getDefaultComponent();
    }
    components[provider] = components[PROVIDER_DEFAULT];

    return components[provider];
  };

  Component.prototype.getUIManagerCommand = function getUIManagerCommand(name) {
    const componentName = getAirComponentName(
      this.context.provider,
      componentType
    );
    return getViewManagerConfig(componentName).Commands[name];
  };

  Component.prototype.getMapManagerCommand = function getMapManagerCommand(
    name
  ) {
    const airComponentName = `${getAirComponentName(
      this.context.provider,
      componentType
    )}Manager`;
    return NativeModules[airComponentName][name];
  };

  return Component;
}
