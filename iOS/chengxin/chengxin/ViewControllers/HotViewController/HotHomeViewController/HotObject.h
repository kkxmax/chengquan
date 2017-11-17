//
//  HotObject.h
//  chengxin
//
//  Created by seniorcoder on 11/7/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HotObject : NSObject
@property(nonatomic, assign) NSInteger          mCommentCnt;
@property(nonatomic, retain) NSString             *strContent;
@property(nonatomic, retain) NSString             *strDownTime;
@property(nonatomic, assign) NSInteger          mElectCnt;
@property(nonatomic, assign) NSInteger          mId;
@property(nonatomic, retain) NSMutableArray       *aryImgPath;
@property(nonatomic, assign) BOOL               bIsFavourite;
@property(nonatomic, assign) NSInteger          mSerial;
@property(nonatomic, assign) NSInteger          mShareCnt;
@property(nonatomic, assign) NSInteger          mStatus;
@property(nonatomic, retain) NSString             *strStatusName;
@property(nonatomic, retain) NSString             *strTitle;
@property(nonatomic, retain) NSString             *strUpTime;
@property(nonatomic, assign) NSInteger          mVisitCnt;
@property(nonatomic, retain) NSDictionary         *dicWriteTime;
@property(nonatomic, retain) NSString             *strWriteTimeString;
@property(nonatomic, assign) NSInteger          mXyleixingId;
@property(nonatomic, assign) NSInteger          mXyleixingLevel1Id;
@property(nonatomic, retain) NSString             *mXyleixingLevel1Name;
@property(nonatomic, retain) NSString             *mXyleixingName;

@end
