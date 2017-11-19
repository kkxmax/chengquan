//
//  CommonData.h
//  chengxin
//
//  Created by seniorcoder on 11/7/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CommonData : NSObject

+ (instancetype) sharedInstance;

@property (nonatomic, strong) NSString *tokenName;
@property (nonatomic, strong) NSDictionary *userInfo;
@property (nonatomic) NSInteger sortOrderIndex; // 1 : 人气 2: 诚信度 3: 最新
@property (nonatomic) NSInteger subHomeIndex;
@property (nonatomic, strong) NSString *choiceCity;
@property (nonatomic, strong) NSString *choiceAkind;
@property (nonatomic, strong) NSString *choiceEnterKind;
@property (nonatomic, strong) NSString *choiceXyleixingIds;
@property (nonatomic, strong) NSString *choicePleixingIds;
@property (nonatomic, strong) NSString *choiceFenleiIds;
@property (nonatomic, strong) NSString *selectedFriendAccountID;
@property (nonatomic, strong) NSString *selectedProductID;
@property (nonatomic, strong) NSDictionary *selectedItemServiceDic;
@property (nonatomic) NSInteger addItemServiceIndex;

@property (nonatomic, strong) NSMutableArray *arrayFamiliarHistory;
@property (nonatomic, strong) NSMutableArray *arrayEnterpriseHistory;
@property (nonatomic, strong) NSMutableArray *arrayProductHistory;
@property (nonatomic, strong) NSMutableArray *arrayItemHistory;
@property (nonatomic, strong) NSMutableArray *arrayServiceHistory;
@property (nonatomic, strong) NSMutableArray *arrayCodeHistory;
@property (nonatomic, strong) NSString *searchFamiliarText;
@property (nonatomic, strong) NSString *searchEnterpriseText;
@property (nonatomic, strong) NSString *searchProductText;
@property (nonatomic, strong) NSString *searchItemText;
@property (nonatomic, strong) NSString *searchServiceText;
@property (nonatomic, strong) NSString *searchCodeText;

@property (nonatomic) long notificationCount;
@end
