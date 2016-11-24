# TanRabad SURVEY
[![Build Status](https://travis-ci.org/tanrabad/survey.svg?branch=master)](https://travis-ci.org/tanrabad/survey)

Survey application of Tanrabad System on android. 

![Main](https://raw.githubusercontent.com/nectec-wisru/android-TanrabadSurvey/master/images/screenshot/main.jpg)

![Survey](https://raw.githubusercontent.com/nectec-wisru/android-TanrabadSurvey/master/images/screenshot/survey.jpg)


## Code Architecture

This project is follow [Clean Architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html) Concept

### Modules
Project have 3 module for easy test and maintenance

- **App** - Android application module contain UI, In-App Database and Api Connection. depend on Domain.
- **Domain** - Java module contain Application's logic. depend on Entity.
- **Entity** - Java module contain Data use Application.

## Libraries
The libraries use in this Project

- Android Support library
- [Android Maps Utils](http://googlemaps.github.io/android-maps-utils/)
- [Google APIs for Android](https://developers.google.com/android/guides/overview)
- [Fabric](https://fabric.io/)
- [OkHttp](https://github.com/square/okhttp)
- [Joda-Time](http://www.joda.org/joda-time/)
- [LoganSquare](https://github.com/bluelinelabs/LoganSquare)
- [Calligraphy](https://github.com/chrisjenx/Calligraphy)
- [Android-Thai-Widget](https://github.com/nectec-wisru/android-ThaiWidget)
- [JumpingBeans](https://github.com/frakbot/JumpingBeans)
- [RecyclerViewHeader](https://github.com/blipinsk/RecyclerViewHeader)
- [License Fragment](https://github.com/first087/Android-License-Fragment)
- [Custom Activity On Crash](https://github.com/Ereza/CustomActivityOnCrash)
- [Android Tooltip](https://github.com/sephiroth74/android-target-tooltip)


## Code Quality Control
This project use quality control by unit-test ui-test and code analysis tools.

Test libraries

- [JUnit](http://junit.org/)
- [Espresso](https://google.github.io/android-testing-support-library/)
- [Mockito](http://mockito.org/)
- [WireMock](http://wiremock.org/)

Static code analysis tools

- [Checkstyle](http://checkstyle.sourceforge.net/) 
- [PMD](https://pmd.github.io/)
- [Findbugs](http://findbugs.sourceforge.net/)
- [Android Check](https://github.com/noveogroup/android-check)

Use [Travis CI](https://travis-ci.org/) as Continuous Integration system

## Requirements

- Java SDK 7 or Above
- [Android SDK](http://developer.android.com/sdk/index.html)
- Android [6.0 (API 23) ](http://developer.android.com/tools/revisions/platforms.html#6.0)
- Android SDK Tools
- Android SDK Build tools 23.0.2
- Android Support Repository

## Contributor

- [Blaze Piruin](https://github.com/Blazei) Agile coach, System Analysis, UI&UX Design,  Programming 
- [N. Choatravee](https://github.com/chncs23) Programming
- [Puy Porntipa](https://github.com/porntipa) Automate Test, Manual Test
- [icesuxx](https://github.com/icesuxx) Research
- [zevendz](https://www.facebook.com/zevendz) Graphics Design

**Before Push Code**

Make sure code pass all test and code quality control before push with 

```cli
./gradlew testBeforePush
```

**Notice** `testBeforePush` task not Include Android-Test (ui-test and some unit-test) 

To run all Unit-test, code quality control and Android-test (Android Emulator or Device Required)

```cli
./gradlew fullTest
```


## License

    Copyright 2015 NECTEC
      National Electronics and Computer Technology Center, Thailand

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    

[![NECTEC](http://www.nectec.or.th/themes/nectec/img/logo.png)](https://www.nectec.or.th)
