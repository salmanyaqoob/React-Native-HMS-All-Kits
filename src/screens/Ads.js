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

import React, {useState, useEffect} from "react";
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  Modal,
  BackHandler,
  Picker,
  ToastAndroid,
  Image,
} from "react-native";

// import Picker from '@react-native-community/picker';

import {EventRegister} from "react-native-event-listeners";

import {headerStyles} from "../styles/headerStyles";

import {Colors} from "react-native/Libraries/NewAppScreen";
import HMSAds, {
  HMSBanner,
  HMSNative,
  HMSInterstitial,
  HMSOaid,
  HMSInstallReferrer,
  HMSSplash,
  HMSReward,
  ConsentStatus,
  DebugNeedConsent,
  AudioFocusType,
  ContentClassification,
  Gender,
  NonPersonalizedAd,
  TagForChild,
  UnderAge,
  NativeAdAssetNames,
  ChoicesPosition,
  Direction,
  BannerAdSizes,
  NativeMediaTypes,
  BannerMediaTypes,
  InterstitialMediaTypes,
  RewardMediaTypes,
  SplashMediaTypes,
  ScaleType,
  CallMode,
  TouchableHighlight,
  TouchableOpacity,
  TouchableWithoutFeedback,
} from "react-native-hms-ads";

const toast = val => {
  ToastAndroid.show(val, ToastAndroid.SHORT);
};

let adsLogs = "";

const Header = () => (
  <View style={headerStyles.headerSection}>
    <View style={headerStyles.headerTitleWrapper}>
      <Text style={headerStyles.headerTitle}>Huawei Ads</Text>
    </View>
    <View style={headerStyles.headerLogoWrapper}>
      <Image
        style={headerStyles.headerLogo}
        source={require("../../assets/images/hms-rn-logo.png")}
      />
    </View>
  </View>
);

const Banner = () => {
  let bannerAdIds = {};
  bannerAdIds[BannerMediaTypes.IMAGE] = "testw6vs28auh3";

  const [bannerAdSize, setBannerAdSize] = useState({
    bannerAdSize: BannerAdSizes.B_320_100,
    // width: 0,
  });
  const [adId, setAdId] = useState(bannerAdIds[BannerMediaTypes.IMAGE]);
  let adBannerElement;
  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.pickerContainer}>
          <Picker
            prompt="Select ad size"
            selectedValue={bannerAdSize.bannerAdSize}
            onValueChange={itemValue =>
              setBannerAdSize({bannerAdSize: itemValue})
            }>
            {Object.values(BannerAdSizes).map(adSize => (
              <Picker.Item label={adSize} value={adSize} key={adSize} />
            ))}
          </Picker>
        </View>
        <Button
          title="Load"
          onPress={() => {
            if (adBannerElement !== null) {
              adBannerElement.loadAd();
            }
          }}
        />
        <Button
          title="Set Refresh"
          color="green"
          onPress={() => {
            if (adBannerElement !== null) {
              adBannerElement.setRefresh(60);
            }
          }}
        />
        <Button
          title="Pause"
          onPress={() => {
            if (adBannerElement !== null) {
              adBannerElement.pause();
            }
          }}
        />
        <Button
          title="Resume"
          color="green"
          onPress={() => {
            if (adBannerElement !== null) {
              adBannerElement.resume();
            }
          }}
        />
        <Button
          title="Destroy"
          color="red"
          onPress={() => {
            if (adBannerElement !== null) {
              adBannerElement.destroy();
            }
          }}
        />
        <HMSBanner
          style={{height: 100}}
          bannerAdSize={bannerAdSize}
          adId={adId}
          adParam={{
            adContentClassification:
              ContentClassification.AD_CONTENT_CLASSIFICATION_UNKOWN,
            // appCountry: '',
            // appLang: '',
            // belongCountryCode: '',
            gender: Gender.UNKNOWN,
            nonPersonalizedAd: NonPersonalizedAd.ALLOW_ALL,
            // requestOrigin: '',
            tagForChildProtection:
              TagForChild.TAG_FOR_CHILD_PROTECTION_UNSPECIFIED,
            tagForUnderAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
            // targetingContentUrl: '',
          }}
          onAdLoaded={e => {
            console.log("HMSBanner onAdLoaded", e.nativeEvent);
            toast("HMSBanner onAdLoaded");
            EventRegister.emit("logsEvent", "HMSBanner onAdLoaded");
          }}
          onAdFailed={e => {
            console.warn("HMSBanner onAdFailed", e.nativeEvent);
            toast("HMSBanner onAdFailed");
            EventRegister.emit("logsEvent", "HMSBanner onAdFailed");
          }}
          onAdOpened={e => {
            toast("HMSBanner onAdOpened");
            EventRegister.emit("logsEvent", "HMSBanner onAdOpened");
          }}
          onAdClicked={e => {
            toast("HMSBanner onAdClicked");
            EventRegister.emit("logsEvent", "HMSBanner onAdClicked");
          }}
          onAdClosed={e => {
            toast("HMSBanner onAdClosed");
            EventRegister.emit("logsEvent", "HMSBanner onAdClosed");
          }}
          onAdImpression={e => {
            toast("HMSBanner onAdImpression");
            EventRegister.emit("logsEvent", "HMSBanner onAdImpression");
          }}
          onAdLeave={e => {
            toast("HMSBanner onAdLeave");
            EventRegister.emit("logsEvent", "HMSBanner onAdLeave");
          }}
          ref={el => {
            adBannerElement = el;
          }}
        />
      </View>
    </>
  );
};

const Native = () => {
  let nativeAdIds = {};
  nativeAdIds[NativeMediaTypes.VIDEO] = "testy63txaom86";
  nativeAdIds[NativeMediaTypes.IMAGE_SMALL] = "testb65czjivt9";
  nativeAdIds[NativeMediaTypes.IMAGE_LARGE] = "testu7m3hc4gvm";

  const [displayForm, setDisplayForm] = useState({
    mediaType: NativeMediaTypes.VIDEO,
    adId: nativeAdIds.video,
  });
  let adNativeElement;
  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.pickerContainer}>
          <Picker
            prompt="Select display form"
            selectedValue={displayForm.mediaType}
            onValueChange={itemValue =>
              setDisplayForm({
                mediaType: itemValue,
                adId: nativeAdIds[itemValue],
              })
            }>
            {Object.values(NativeMediaTypes).map(mType => (
              <Picker.Item label={mType} value={mType} key={mType} />
            ))}
          </Picker>
        </View>
        <Button
          title="Load"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.loadAd();
            }
          }}
        />
        <Button
          title="Dislike"
          color="orange"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.dislikeAd("Because I dont like it");
            }
          }}
        />
        <Button
          title="Go to Why Page"
          color="purple"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.gotoWhyThisAdPage();
            }
          }}
        />
        <Button
          title="Destroy"
          color="red"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.destroy();
            }
          }}
        />
        <Button
          title="Allow custom click"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.setAllowCustomClick();
            }
          }}
        />
        <Button
          title="Record click event"
          color="green"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.recordClickEvent();
            }
          }}
        />
        <Button
          title="Record impression"
          color="red"
          onPress={() => {
            if (adNativeElement !== null) {
              adNativeElement.recordImpressionEvent({
                impressed: true,
                isUseful: "nope",
              });
            }
          }}
        />
      </View>
      <View>
        <HMSNative
          style={{height: 322}}
          displayForm={displayForm}
          adParam={{
            adContentClassification:
              ContentClassification.AD_CONTENT_CLASSIFICATION_UNKOWN,
            // appCountry: '',
            // appLang: '',
            // belongCountryCode: '',
            gender: Gender.UNKNOWN,
            nonPersonalizedAd: NonPersonalizedAd.ALLOW_ALL,
            // requestOrigin: '',
            tagForChildProtection:
              TagForChild.TAG_FOR_CHILD_PROTECTION_UNSPECIFIED,
            tagForUnderAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
            // targetingContentUrl: '',
          }}
          nativeConfig={{
            choicesPosition: ChoicesPosition.BOTTOM_RIGHT,
            mediaDirection: Direction.ANY,
            // mediaAspect: 2,
            // requestCustomDislikeThisAd: false,
            // requestMultiImages: false,
            // returnUrlsForImages: false,
            // adSize: {
            //   height: 100,
            //   width: 100,
            // },
            videoConfiguration: {
              audioFocusType: AudioFocusType.NOT_GAIN_AUDIO_FOCUS_ALL,
              // clickToFullScreenRequested: true,
              // customizeOperateRequested: true,
              startMuted: true,
            },
          }}
          viewOptions={{
            showMediaContent: false,
            mediaImageScaleType: ScaleType.FIT_CENTER,
            // adSourceTextStyle: {color: 'red'},
            // adFlagTextStyle: {backgroundColor: 'red', fontSize: 10},
            // titleTextStyle: {color: 'red'},
            descriptionTextStyle: {visibility: false},
            callToActionStyle: {color: "black", fontSize: 12},
          }}
          onNativeAdLoaded={e => {
            console.log("HMSNative onNativeAdLoaded", e.nativeEvent);
            toast("HMSNative onNativeAdLoaded");
            EventRegister.emit("logsEvent", "HMSNative onNativeAdLoaded");
          }}
          onAdDisliked={e => toast("HMSNative onAdDisliked")}
          onAdFailed={e => {
            console.warn("HMSNative onAdFailed", e.nativeEvent);
            toast("HMSNative onAdFailed");
            EventRegister.emit("logsEvent", "HMSNative onAdFailed");
          }}
          onAdClicked={e => {
            toast("HMSNative onAdClicked");
            EventRegister.emit("logsEvent", "HMSNative onAdClicked");
          }}
          onAdImpression={e => {
            toast("HMSNative onAdImpression");
            EventRegister.emit("logsEvent", "HMSNative onAdImpression");
          }}
          onVideoStart={e => {
            toast("HMSNative onVideoStart");
            EventRegister.emit("logsEvent", "HMSNative onVideoStart");
          }}
          onVideoPlay={e => {
            toast("HMSNative onVideoPlay");
            EventRegister.emit("logsEvent", "HMSNative onVideoPlay");
          }}
          onVideoEnd={e => {
            toast("HMSNative onVideoEnd");
            EventRegister.emit("logsEvent", "HMSNative onVideoEnd");
          }}
          ref={el => {
            adNativeElement = el;
          }}
        />
      </View>
    </>
  );
};

const Interstitial = () => {
  let interstitialAdIds = {};
  interstitialAdIds[InterstitialMediaTypes.IMAGE] = "teste9ih9j0rc3";
  interstitialAdIds[InterstitialMediaTypes.VIDEO] = "testb4znbuh3n2";

  const [isLoaded, setLoaded] = useState(false);
  const [displayForm, setDisplayForm] = useState({
    mediaType: InterstitialMediaTypes.VIDEO,
    adId: interstitialAdIds.video,
  });
  useEffect(() => {
    HMSInterstitial.setAdId(displayForm.adId);
    HMSInterstitial.adClosedListenerAdd(() => {
      toast("HMSInterstitial adClosed");
      EventRegister.emit("logsEvent", "HMSInterstitial adClosed");
    });
    // HMSInterstitial.adClosedListenerRemove();

    HMSInterstitial.adFailedListenerAdd(error => {
      toast("HMSInterstitial adFailed");
      console.warn("HMSInterstitial adFailed, error: ", error);
      EventRegister.emit(
        "logsEvent",
        "HMSInterstitial adFailed, error: " + JSON.stringify(error),
      );
    });
    // HMSInterstitial.adFailedListenerRemove();

    HMSInterstitial.adLeaveListenerAdd(() => {
      toast("HMSInterstitial adLeave");
      EventRegister.emit("logsEvent", "HMSInterstitial adLeave");
    });
    // HMSInterstitial.adLeaveListenerRemove();

    HMSInterstitial.adOpenedListenerAdd(() => {
      toast("HMSInterstitial adOpened");
      EventRegister.emit("logsEvent", "HMSInterstitial adOpened");
    });
    // HMSInterstitial.adOpenedListenerRemove();

    HMSInterstitial.adLoadedListenerAdd(result => {
      toast("HMSInterstitial adLoaded");
      console.log("HMSInterstitial adLoaded, result: ", result);
      EventRegister.emit(
        "logsEvent",
        "HMSInterstitial adLoaded, result: " + JSON.stringify(result),
      );
    });
    // HMSInterstitial.adLoadedListenerRemove();

    HMSInterstitial.adClickedListenerAdd(() => {
      toast("HMSInterstitial adClicked");
      EventRegister.emit("logsEvent", "HMSInterstitial adClicked");
    });
    // HMSInterstitial.adClickedListenerRemove();

    HMSInterstitial.adImpressionListenerAdd(() => {
      toast("HMSInterstitial adImpression");
      EventRegister.emit("logsEvent", "HMSInterstitial adImpression");
    });
    // HMSInterstitial.adImpressionListenerRemove();

    return HMSInterstitial.allListenersRemove;
  }, [displayForm]);
  return (
    <>
      <View>
        <View style={styles.sectionContainer}>
          <View style={styles.pickerContainer}>
            <Picker
              prompt="Media Type"
              selectedValue={displayForm.mediaType}
              style={styles.picker}
              onValueChange={itemValue => {
                setDisplayForm({
                  mediaType: itemValue,
                  adId: interstitialAdIds[itemValue],
                });
                HMSInterstitial.setAdId(interstitialAdIds[itemValue]);
              }}>
              {Object.values(InterstitialMediaTypes).map(mType => (
                <Picker.Item label={mType} value={mType} key={mType} />
              ))}
            </Picker>
          </View>
          <Button
            title="Load"
            onPress={() => {
              HMSInterstitial.loadAd();
            }}
          />
          <Button
            title="Set Ad Parameter"
            onPress={() => {
              HMSInterstitial.setAdParam({
                adContentClassification:
                  ContentClassification.AD_CONTENT_CLASSIFICATION_UNKOWN,
                // appCountry: '',
                // appLang: '',
                // belongCountryCode: '',
                gender: Gender.UNKNOWN,
                nonPersonalizedAd: NonPersonalizedAd.ALLOW_ALL,
                // requestOrigin: '',
                tagForChildProtection:
                  TagForChild.TAG_FOR_CHILD_PROTECTION_UNSPECIFIED,
                tagForUnderAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
                // targetingContentUrl: '',
              });
            }}
          />
          <Button
            color="green"
            title="Check"
            onPress={() => {
              HMSInterstitial.isLoaded().then(result => {
                toast(`Interstitial ad is ${result ? "" : "not"} loaded`);
                setLoaded(result);
                EventRegister.emit(
                  "logsEvent",
                  `Interstitial ad is ${result ? "" : "not"} loaded`,
                );
              });
            }}
          />
          <Button
            title="Show"
            color="purple"
            disabled={!isLoaded}
            onPress={() => {
              setLoaded(false);
              HMSInterstitial.show();
            }}
          />
        </View>
      </View>
    </>
  );
};

const Reward = () => {
  let rewardAdIds = {};
  rewardAdIds[RewardMediaTypes.VIDEO] = "testx9dtjwj8hp";
  const [isLoaded, setLoaded] = useState(false);
  const [displayForm, setDisplayForm] = useState({
    mediaType: RewardMediaTypes.VIDEO,
    adId: rewardAdIds[RewardMediaTypes.VIDEO],
  });
  // console.log(displayForm);
  useEffect(() => {
    HMSReward.setAdId(displayForm.adId);
    HMSReward.setVerifyConfig({userId: "HMS_User", data: "HMS data"});

    HMSReward.adLoadedListenerAdd(result => {
      console.log("HMSReward adLoaded, result: ", result);
      toast("HMSReward adLoaded");
      EventRegister.emit("logsEvent", "HMSReward adLoaded");
    });
    // HMSReward.adLoadedListenerRemove();

    HMSReward.adFailedToLoadListenerAdd(error => {
      toast("HMSReward adFailedToLoad");
      console.warn("HMSReward adFailedToLoad, error: ", error);
      EventRegister.emit(
        "logsEvent",
        "HMSReward adFailedToLoad, error: " + JSON.stringify(error),
      );
    });
    // HMSReward.adFailedToLoadListenerRemove();

    HMSReward.adFailedToShowListenerAdd(error => {
      toast("HMSReward adFailedToShow");
      console.warn("HMSReward adFailedToShow, error: ", error);
      EventRegister.emit(
        "logsEvent",
        "HMSReward adFailedToShow, error: " + JSON.stringify(error),
      );
    });
    // HMSReward.adFailedToShowListenerRemove();

    HMSReward.adOpenedListenerAdd(() => {
      toast("HMSReward adOpened");
      EventRegister.emit("logsEvent", "HMSReward adOpened");
    });
    // HMSReward.adOpenedListenerRemove();

    HMSReward.adClosedListenerAdd(() => {
      toast("HMSReward adClosed");
      EventRegister.emit("logsEvent", "HMSReward adClosed");
    });
    // HMSReward.adClosedListenerRemove();

    HMSReward.adRewardedListenerAdd(reward => {
      toast("HMSReward adRewarded");
      console.log("HMSReward adRewarded, reward: ", reward);
      EventRegister.emit(
        "logsEvent",
        "HMSReward adRewarded, reward: " + JSON.stringify(reward),
      );
    });
    // HMSReward.adRewardedListenerRemove();

    return HMSReward.allListenersRemove;
  }, [displayForm]);

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.pickerContainer}>
          <Picker
            prompt="Media Type"
            selectedValue={displayForm.mediaType}
            style={styles.picker}
            onValueChange={itemValue => {
              setDisplayForm({
                mediaType: itemValue,
                adId: rewardAdIds[itemValue],
              });
              HMSReward.setAdId(rewardAdIds[itemValue]);
            }}>
            {Object.values(RewardMediaTypes).map(mType => (
              <Picker.Item label={mType} value={mType} key={mType} />
            ))}
          </Picker>
        </View>
        <Button
          title="Load"
          onPress={() => {
            HMSReward.loadAd();
          }}
        />
        {/* <Button
          title="Set Ad Parameter"
          onPress={() => {
            HMSReward.setAdParam({
              adContentClassification:
                ContentClassification.AD_CONTENT_CLASSIFICATION_UNKOWN,
              // appCountry: '',
              // appLang: '',
              // belongCountryCode: '',
              gender: Gender.UNKNOWN,
              nonPersonalizedAd: NonPersonalizedAd.ALLOW_ALL,
              // requestOrigin: '',
              tagForChildProtection:
                TagForChild.TAG_FOR_CHILD_PROTECTION_UNSPECIFIED,
              tagForUnderAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
              // targetingContentUrl: '',
            });
          }}
        /> */}
        <Button
          color="green"
          title="Check"
          onPress={() => {
            HMSReward.isLoaded().then(result => {
              toast(`Reward ad is ${result ? "" : "not"} loaded`);
              setLoaded(result);
              EventRegister.emit(
                "logsEvent",
                `Reward ad is ${result ? "" : "not"} loaded`,
              );
            });
          }}
        />
        <Button
          title="Show"
          disabled={!isLoaded}
          onPress={() => {
            setLoaded(false);
            HMSReward.show();
          }}
        />
      </View>
    </>
  );
};

const Splash = () => {
  const [mediaType, setMediaType] = useState(SplashMediaTypes.VIDEO);
  let splashAdIds = {};
  splashAdIds[SplashMediaTypes.VIDEO] = "testd7c5cewoj6";
  splashAdIds[SplashMediaTypes.IMAGE] = "testq6zq98hecj";

  HMSSplash.setLogoText("HMS App");
  HMSSplash.setCopyrightText("Copyright HMS");

  useEffect(() => {
    HMSSplash.adLoadedListenerAdd(() => {
      toast("HMSSplash adLoaded");
      EventRegister.emit("logsEvent", "HMSSplash adLoaded");
    });
    // HMSSplash.adLoadedListenerRemove();
    HMSSplash.adFailedToLoadListenerAdd(() => {
      toast("HMSSplash adFailedToLoad");
      EventRegister.emit("logsEvent", "HMSSplash adFailedToLoad");
    });
    // HMSSplash.adFailedToLoadListenerRemove();
    HMSSplash.adDismissedListenerAdd(() => {
      toast("HMSSplash adDismissed");
      EventRegister.emit("logsEvent", "HMSSplash adDismissed");
    });
    // HMSSplash.adDismissedListenerRemove();
    HMSSplash.adShowedListenerAdd(() => {
      toast("HMSSplash adShowed");
      EventRegister.emit("logsEvent", "HMSSplash adShowed");
    });
    // HMSSplash.adShowedListenerRemove();
    HMSSplash.adClickListenerAdd(() => {
      toast("HMSSplash adClick");
      EventRegister.emit("logsEvent", "HMSSplash adClick");
    });
    // HMSSplash.adClickListenerRemove();

    // HMSSplash.setAdId('testq6zq98hecj');
    // HMSSplash.setSloganResource('example_slogan');
    // HMSSplash.show();
    // HMSSplash.isLoading()
    //   .then((res) => console.log('XXX', res))
    //   .catch((e) => console.log('XXX', e));
    // HMSSplash.isLoaded().then((res) => console.log('XXX', res));
    // setTimeout(function () {
    //   HMSSplash.pause();
    // }, 2000);

    // setTimeout(function () {
    //   HMSSplash.resume();
    // }, 3000);
    // setTimeout(function () {
    //   HMSSplash.isLoading()
    //     .then((res) => console.log('XXX', res))
    //     .catch((e) => console.log('XXX', e));
    // }, 3000);
    // HMSSplash.pause();

    return HMSSplash.allListenersRemove;
  }, []);

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.pickerContainer}>
          <Picker
            prompt="Media Type"
            selectedValue={mediaType}
            style={styles.picker}
            onValueChange={itemValue => {
              setMediaType(itemValue);
              HMSSplash.setAdId(splashAdIds[itemValue]);
            }}>
            {Object.values(SplashMediaTypes).map(mType => (
              <Picker.Item label={mType} value={mType} key={mType} />
            ))}
          </Picker>
        </View>
        <Button title="Splash" color="green" onPress={() => HMSSplash.show()} />
        <Button
          title="Set Ad Parameter"
          onPress={() => {
            HMSSplash.setAdParam({
              adContentClassification:
                ContentClassification.AD_CONTENT_CLASSIFICATION_UNKOWN,
              // appCountry: '',
              // appLang: '',
              // belongCountryCode: '',
              gender: Gender.UNKNOWN,
              nonPersonalizedAd: NonPersonalizedAd.ALLOW_ALL,
              // requestOrigin: '',
              tagForChildProtection:
                TagForChild.TAG_FOR_CHILD_PROTECTION_UNSPECIFIED,
              tagForUnderAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
              // targetingContentUrl: '',
            });
          }}
        />
      </View>
    </>
  );
};

const AdvertisingId = () => {
  const [advertisingInfo, setAdvertisingInfo] = useState({
    id: "-",
    isLimitAdTrackingEnabled: false,
  });
  const [callMode, setCallMode] = useState(CallMode.SDK);

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.pickerContainer}>
          <Picker
            prompt="Select Call Mode"
            selectedValue={callMode}
            onValueChange={itemValue => setCallMode(itemValue)}>
            {Object.values(CallMode).map(cMode => (
              <Picker.Item label={cMode} value={cMode} key={cMode} />
            ))}
          </Picker>
        </View>
        <Button
          title="Get Advertising Id Info"
          onPress={() =>
            HMSOaid.getAdvertisingIdInfo(callMode)
              .then(result => {
                console.log("HMSOaid getAdvertisingIdInfo, result:", result);
                EventRegister.emit(
                  "logsEvent",
                  "HMSOaid getAdvertisingIdInfo, result:" +
                    JSON.stringify(result),
                );
                setAdvertisingInfo(result);
              })
              .catch(e =>
                console.log("HMSOaid getAdvertisingIdInfo, error:", e),
              )
          }
        />
        <Button
          title="Clear"
          color="red"
          onPress={() =>
            setAdvertisingInfo({
              id: "-",
              isLimitAdTrackingEnabled: false,
            })
          }
        />
        <Text title="Advertising Id">
          Advertising Id : {advertisingInfo.id}
        </Text>
        <Text title="Limit Ad Tracking Enabled">
          Limit Ad Tracking Enabled :
          {advertisingInfo.isLimitAdTrackingEnabled ? "True" : "False"}
        </Text>

        <Button
          color="green"
          title="Verify Advertising Id"
          onPress={() =>
            HMSOaid.verifyAdvertisingId(advertisingInfo)
              .then(result => {
                console.log("HMSOaid verifyAdvertisingId, result:", result);
                // eslint-disable-next-line no-alert
                // alert(result ? "Verified" : "Not verified");
                EventRegister.emit(
                  "logsEvent",
                  result ? "Verified" : "Not verified",
                );
              })
              .catch(e => {
                console.warn("HMSOaid verifyAdvertisingId, error:", e);
                // eslint-disable-next-line no-alert
                // alert("Not verified, error", e);
                EventRegister.emit("logsEvent", "Not verified, error" + e);
              })
          }
        />
      </View>
    </>
  );
};

const InstallReferrer = () => {
  const isTest = true;
  const pkgName = "com.rnhmsadsdemo";
  const [callMode, setCallMode] = useState(CallMode.SDK);

  useEffect(() => {
    HMSInstallReferrer.serviceConnectedListenerAdd(response => {
      toast("HMSInstallReferrer serviceConnected");
      console.log("HMSInstallReferrer serviceConnected, response:", response);
      EventRegister.emit(
        "logsEvent",
        "HMSInstallReferrer serviceConnected, response:" +
          JSON.stringify(response),
      );
    });
    // HMSInstallReferrer.serviceConnectedListenerRemove();
    HMSInstallReferrer.serviceDisconnectedListenerAdd(() => {
      toast("HMSInstallReferrer serviceDisconnected");
      console.log("HMSInstallReferrer serviceDisconnected");
      EventRegister.emit("logsEvent", "HMSInstallReferrer serviceDisconnected");
    });
    // HMSInstallReferrer.serviceDisconnectedListenerRemove();

    return HMSInstallReferrer.allListenersRemove;
  }, []);

  return (
    <>
      <View>
        <View style={styles.sectionContainer}>
          <View style={styles.pickerContainer}>
            <Picker
              style={styles.picker}
              prompt="Select Call Mode"
              selectedValue={callMode}
              onValueChange={itemValue => setCallMode(itemValue)}>
              {Object.values(CallMode).map(cMode => (
                <Picker.Item label={cMode} value={cMode} key={cMode} />
              ))}
            </Picker>
          </View>

          <Button
            title="Start Install Referer with given call mode"
            onPress={() =>
              HMSInstallReferrer.startConnection(callMode, isTest, pkgName)
                .then(result => {
                  console.log(
                    "HMSInstallReferrer startConnection, result:",
                    result,
                  );
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer startConnection, result:" +
                      JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.warn("HMSInstallReferrer startConnection, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer startConnection, error:" +
                      JSON.stringify(e),
                  );
                })
            }
          />
          <Button
            color="green"
            title="Ready?"
            onPress={() =>
              HMSInstallReferrer.isReady()
                .then(result => {
                  console.log("HMSInstallReferrer isReady, result:", result);
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer isReady, result:" +
                      JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.warn("HMSInstallReferrer isReady, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer isReady, error:" + JSON.stringify(e),
                  );
                })
            }
          />
          <Button
            color="purple"
            title="Get Referrer Details"
            onPress={() =>
              HMSInstallReferrer.getReferrerDetails()
                .then(result => {
                  console.log(
                    "HMSInstallReferrer getReferrerDetails, result:",
                    result,
                  );
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer getReferrerDetails, result:" +
                      JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.warn(
                    "HMSInstallReferrer getReferrerDetails, error:",
                    e,
                  );
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer getReferrerDetails, error:" +
                      JSON.stringify(e),
                  );
                })
            }
          />
          <Button
            color="red"
            title="End Install Referer connection"
            onPress={() =>
              HMSInstallReferrer.endConnection()
                .then(() => {
                  console.log("HMSInstallReferrer endConnection");
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer endConnection",
                  );
                })
                .catch(e => {
                  console.warn("HMSInstallReferrer endConnection, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMSInstallReferrer endConnection, error:" +
                      JSON.stringify(e),
                  );
                })
            }
          />
        </View>
      </View>
    </>
  );
};

const Consent = () => {
  return (
    <>
      <View>
        <View style={styles.sectionContainer}>
          <Button
            title="Set Consent"
            onPress={() =>
              HMSAds.setConsent({
                consentStatus: ConsentStatus.NON_PERSONALIZED,
                debugNeedConsent: DebugNeedConsent.DEBUG_NEED_CONSENT,
                underAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
                // testDeviceId: '********',
              })
                .then(result => {
                  console.log("HMS setConsent, result:", result);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS setConsent, result:" + JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.warn("HMS setConsent, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS setConsent, error:" + JSON.stringify(e),
                  );
                })
            }
          />
          <Button
            color="green"
            title="Check Consent"
            onPress={() =>
              HMSAds.checkConsent()
                .then(result => {
                  console.log("HMS checkConsent, result:", result);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS checkConsent, result:" + JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.log("HMS checkConsent, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS checkConsent, error:" + JSON.stringify(e),
                  );
                })
            }
          />
        </View>
      </View>
    </>
  );
};

const RequestOptions = () => {
  return (
    <>
      <View>
        <View style={styles.sectionContainer}>
          <Button
            title="Set Request"
            onPress={() =>
              HMSAds.setRequestOptions({
                adContentClassification:
                  ContentClassification.AD_CONTENT_CLASSIFICATION_A,
                // appCountry: AppCountry,
                // appLang: AppLang,
                nonPersonalizedAd: NonPersonalizedAd.ALLOW_ALL,
                tagForChildProtection:
                  TagForChild.TAG_FOR_CHILD_PROTECTION_UNSPECIFIED,
                tagForUnderAgeOfPromise: UnderAge.PROMISE_UNSPECIFIED,
              })
                .then(result => {
                  console.log("HMS setRequestOptions, result:", result);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS setRequestOptions, result:" + JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.warn("HMS setRequestOptions, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS setRequestOptions, error:" + JSON.stringify(e),
                  );
                })
            }
          />
          <Button
            title="Get Request"
            color="green"
            onPress={() =>
              HMSAds.getRequestOptions()
                .then(result => {
                  console.log("HMS getRequestOptions, result:", result);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS getRequestOptions, result:" + JSON.stringify(result),
                  );
                })
                .catch(e => {
                  console.warn("HMS getRequestOptions, error:", e);
                  EventRegister.emit(
                    "logsEvent",
                    "HMS getRequestOptions, error:" + JSON.stringify(e),
                  );
                })
            }
          />
        </View>
      </View>
    </>
  );
};

const pages = [
  {
    name: "Splash Ad",
    id: "splash",
    component: <Splash key="splash" />,
  },
  {name: "Reward Ad", id: "reward", component: <Reward key="reward" />},
  {
    name: "Interstitial Ad",
    id: "interstitial",
    component: <Interstitial key="interstitial" />,
  },
  {name: "Native Ad", id: "native", component: <Native key="native" />},
  {name: "Banner", id: "banner", component: <Banner key="banner" />},
  {
    name: "Advertising Id",
    id: "advertisingInfo",
    component: <AdvertisingId key="advertisingInfo" />,
  },

  {
    name: "Install Referrer",
    id: "installReferrer",
    component: <InstallReferrer key="installReferrer" />,
  },
  {name: "Consent", id: "consent", component: <Consent key="consent" />},
  {
    name: "Request Options",
    id: "requestOptions",
    component: <RequestOptions key="requestOptions" />,
  },
];

const initAppState = {
  privacyEnabled: true,
  consentEnabled: true,
  consentIgnored: false,
  pageId: pages[0].id,
  logData: "",
};

const clearLogs = () => {};

class AdsPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = initAppState;
    this.clearLog = this.clearLog.bind(this);
    this.setLogs = this.setLogs.bind(this);
  }

  clearLog() {
    toast("Clear Logs");

    adsLogs = "";
    console.log("start clearLog");
    this.setState({
      logData: adsLogs,
    });
  }

  setLogs(msg) {
    adsLogs = adsLogs + msg + "\n";
    this.setState({
      logData: adsLogs,
    });
  }
  UNSAFE_componentWillMount() {
    this.listener = EventRegister.addEventListener("logsEvent", data => {
      this.setLogs(data);
    });
  }
  componentWillUnmount() {
    EventRegister.removeEventListener(this.listener);
  }

  render() {
    const usingHermes =
      typeof global.HermesInternal === "object" &&
      global.HermesInternal !== null;

    const {privacyEnabled, consentEnabled, consentIgnored, pageId} = this.state;
    return (
      <>
        <Modal
          animationType="slide"
          transparent={false}
          visible={!privacyEnabled}
          onRequestClose={() => {
            BackHandler.exitApp();
          }}>
          <Text style={styles.sectionDescription}>
            Privacy Example of HUAWEI X
          </Text>
          <Text style={styles.sectionContainer}>
            1.Privacy description {"\n"}
            {"\n"} The RNHmsAdsDemois software providing a code demo for the
            HUAWEI Ads SDK. Connecting to the network, the software collects and
            processes information to identify devices, providing customized
            services or ads. If you do not agree to collect the preceding
            information or do not agree to call related permissions or functions
            of your mobile phones, the software cannot run properly. You can
            stop data collection and uploading by uninstalling or exiting this
            software. {"\n"}
            {"\n"} 2.Demo description
            {"\n"}
            {"\n"} This demo is for reference only. Modify the content based on
            the user protocol specifications. {"\n"}
            {"\n"} 3.Advertising and marketing {"\n"}
            {"\n"} We will create a user group based on your personal
            information, collect your device information, usage information, and
            ad interaction information in this app, and display more relevant
            personalized ads and other promotion content. In this process, we
            will strictly protect your privacy. You can learn more about how we
            collect and use your personal information in personalized ads based
            on Ads and Privacy. If you want to restrict personalized ads, you
            can tap here to open the ad setting page and enable the function of
            restricting personalized ads. After the function is enabled, you
            will still receive equivalent number of ads. However, the ad
            relevance will be reduced.
          </Text>
          <View style={styles.container10Top}>
            <Button
              title="I Agree"
              onPress={() => {
                this.setState({privacyEnabled: true});
              }}
            />
            <Button
              title="Close"
              color="red"
              onPress={() => BackHandler.exitApp()}
            />
          </View>
        </Modal>
        <Modal
          animationType="slide"
          transparent={false}
          visible={privacyEnabled && !consentIgnored && !consentEnabled}
          onRequestClose={() => {
            this.setState({consentIgnored: true});
          }}>
          <Text style={styles.sectionDescription}>
            Content Example of HUAWEI X
          </Text>
          <Text style={styles.sectionContainer}>
            The Ads in HUAWEI X is provided in collaboration with our partners.
            You can find a full list of our partners for each country/region
            here.{"\n"}
            {"\n"} In order to provide you with personalized advertisements, we
            need to share the following information with our partners:{"\n"}
            {"\n"} • User information, including advertising ID, city of
            residence, country, and language.{"\n"}
            {"\n"} • Device information, including device name and model,
            operating system version, screen size, and network type.{"\n"}
            {"\n"} • Service usage information, including news ID and records of
            views, clicks, dislikes, shares, and comments for news content and
            advertisements.{"\n"}
            {"\n"} With your consent, the above information will be shared with
            our partners so that they can provide you with personalized
            advertisements on behalf of their customers, based on interests and
            preferences identified or predicted through analysis of your
            personal information.{"\n"}
            {"\n"} You can withdraw your consent at any time by going to app
            settings.{"\n"}
            {"\n"} Without your consent, no data will be shared with our
            partners
          </Text>
          <View style={{height: 10}} />
          <View style={styles.container10Top}>
            <Button
              title="I Agree"
              onPress={() => {
                this.setState({consentEnabled: true});
              }}
            />
            <Button
              title="Ignore"
              onPress={() => {
                this.setState({consentIgnored: true});
              }}
            />
          </View>
        </Modal>
        <StatusBar barStyle="light-content" />
        <SafeAreaView>
          <ScrollView
            contentInsetAdjustmentBehavior="automatic"
            style={styles.scrollView}>
            {!usingHermes ? null : (
              <View style={styles.engine}>
                <Text style={styles.footer}>Engine: Hermes</Text>
              </View>
            )}
            <Header />
            <View style={styles.body}>
              <View style={styles.container10Top}>
                <Button
                  title="Ask Permissions"
                  color="red"
                  onPress={() => {
                    this.setState({
                      privacyEnabled: false,
                      consentEnabled: false,
                    });
                  }}
                />
                <Button
                  title="Init"
                  color="green"
                  onPress={() =>
                    HMSAds.init()
                      .then(result => {
                        console.log("HMS init, result:", result);
                        EventRegister.emit(
                          "logsEvent",
                          "HMS init, result:" + JSON.stringify(result),
                        );
                      })
                      .catch(e => {
                        console.warn("HMS init, error:", e);
                        EventRegister.emit(
                          "logsEvent",
                          "HMS init, error:" + JSON.stringify(e),
                        );
                      })
                  }
                />
                <Button title="ClearLog" onPress={this.clearLog}>
                  ClearLog
                </Button>
              </View>
            </View>
            {!privacyEnabled ? null : (
              <View style={styles.sectionContainerMain}>
                <ScrollView>
                  <Text style={styles.sectionHeader}>Logs</Text>
                  {this.state.logData == "" ? null : (
                    <Text>{this.state.logData}</Text>
                  )}
                </ScrollView>
                <Text style={styles.sectionHeader} title="Functions">
                  Huawei Ads Type
                </Text>
                <View style={styles.pickerContainer}>
                  <Picker
                    style={styles.pickerLarge}
                    prompt="Select Function"
                    selectedValue={pageId}
                    onValueChange={itemValue =>
                      this.setState({pageId: itemValue})
                    }>
                    {pages.map(page => (
                      <Picker.Item
                        label={page.name}
                        value={page.id}
                        key={page.id}
                      />
                    ))}
                  </Picker>
                </View>

                {pages
                  .filter(page => page.id === pageId)
                  .map(page => page.component)}
              </View>
            )}
          </ScrollView>
        </SafeAreaView>
      </>
    );
  }
}

const styles = StyleSheet.create({
  scrollView: {
    // backgroundColor: Colors.lighter,
    paddingBottom: 20,
  },
  engine: {
    position: "absolute",
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
    padding: 10,
  },
  sectionContainerMain: {
    paddingHorizontal: 24,
  },
  sectionContainer: {
    marginTop: 5,
    paddingHorizontal: 0,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: "600",
    color: Colors.black,
  },
  sectionTitleSmall: {
    fontSize: 14,
    fontWeight: "600",
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: "400",
    color: Colors.dark,
  },
  highlight: {
    fontWeight: "700",
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: "600",
    padding: 4,
    paddingRight: 12,
    textAlign: "right",
  },
  buttons: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "stretch",
  },
  pickerContainer: {
    borderWidth: 1,
    borderRadius: 5,
    borderStyle: "dashed",
    borderColor: Colors.dark,
    marginBottom: 3,
  },
  pickerLarge: {
    borderRadius: 5,
    borderColor: Colors.lighter,
    borderLeftWidth: 3,
    borderStyle: "dashed",
  },
  picker: {
    // height: 50,
    // width: 110,
    borderWidth: 1,
    borderRadius: 5,
    borderStyle: "dashed",
    borderColor: Colors.lighter,
  },
  sectionHeader: {
    fontSize: 20,
    fontWeight: "600",
    color: Colors.black,
    paddingBottom: 12,
    paddingHorizontal: 0,
  },
  yellowText: {backgroundColor: "yellow"},
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
  container10Top: {
    flexDirection: "row",
    justifyContent: "space-around",
    // marginBottom: 5,
  },
});

export default AdsPage;
