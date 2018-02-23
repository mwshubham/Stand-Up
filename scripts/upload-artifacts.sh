#!/usr/bin/env bash

#Prepare output folder
if [ -f ./output ]; then
    mkdir ./output
fi

# Move cloc file for code language stats
if [ -f ./cloc.txt ]; then
    mv ./cloc.txt ./output
fi

#project level artifacts
if [ -f ./reports/profile ]; then
    mv ./reports/profile ./output/profile/
fi
if [ -f ./build/reports/dependencyCheck ]; then
    mv ./build/reports/dependencyCheck ./output/dependencyCheck/
fi

#app module data
if [ -f ./app/build/outputs/apk ]; then
    mv ./app/build/outputs/apk ./output/
fi
if [ -f ./app/build/outputs/dexcount ]; then
    mv ./app/build/outputs/dexcount ./output/app/
fi
if [ -f ./app/build/reports ]; then
    mv ./app/build/reports ./output/app/
fi

#common module
if [ -f ./common/build/reports ]; then
    mv ./common/build/reports ./output/common/
fi

#core module
if [ -f ./core/build/reports ]; then
    mv ./core/build/reports ./output/core/
fi

#facebook-auth module
if [ -f ./facebook-auth/build/reports ]; then
    mv ./facebook-auth/build/reports ./output/facebook-auth/
fi

#google-auth module
if [ -f ./google-auth/build/reports ]; then
    mv ./google-auth/build/reports ./output/google-auth/
fi

#network module
if [ -f ./network/build/reports ]; then
    mv ./network/build/reports ./output/network/
fi

#ruler-view module
if [ -f ./ruler-view/build/reports ]; then
    mv ./ruler-view/build/reports ./output/ruler-view/
fi

#progress-button module
if [ -f ./progress-button/build/reports ]; then
    mv ./progress-button/build/reports ./output/progress-button/
fi

#spinner-time-picker module
if [ -f ./spinner-time-picker/build/reports ]; then
    mv ./spinner-time-picker/build/reports ./output/spinner-time-picker/
fi

#timeline-view module
if [ -f ./timeline-view/build/reports ]; then
    mv ./timeline-view/build/reports ./output/timeline-view/
fi

#utils module
if [ -f ./utils/build/reports ]; then
    mv ./utils/build/reports ./output/utils/
fi

#feature-about module
if [ -f ./feature-about/build/reports ]; then
    mv ./feature-about/build/reports ./output/feature-about/
fi

#feature-authentication module
if [ -f ./feature-authentication/build/reports ]; then
    mv ./feature-authentication/build/reports ./output/feature-authentication/
fi

#feature-dashboard module
if [ -f ./feature-dashboard/build/reports ]; then
    mv ./feature-dashboard/build/reports ./output/feature-dashboard/
fi

#feature-diary module
if [ -f ./feature-diary/build/reports ]; then
    mv ./feature-diary/build/reports ./output/feature-diary/
fi

#feature-profile module
if [ -f ./feature-profile/build/reports ]; then
    mv ./feature-profile/build/reports ./output/feature-profile/
fi

if [ -f ./feature-diary/build/reports ]; then
#feature-settings module
    mv ./feature-settings/build/reports ./output/feature-settings/
fi

#feature-stats module
if [ -f ./feature-stats/build/reports ]; then
    mv ./feature-stats/build/reports ./output/feature-stats/
fi

##Upload to artifacts to drop box
#tar cvf artifacts-$TRAVIS_BUILD_NUMBER-$TRAVIS_EVENT_TYPE.tar output
#chmod +x ./scripts/dropbox_uploader.sh
#bash ./scripts/dropbox_uploader.sh -p -h upload artifacts-$TRAVIS_BUILD_NUMBER-$TRAVIS_EVENT_TYPE.tar /Stand-Up/Artifacts/
