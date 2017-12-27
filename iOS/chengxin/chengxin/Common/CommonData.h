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

- (void)clearData;
@property (nonatomic, retain) NSString *tokenName;
@property (nonatomic, retain) NSDictionary *userInfo;
@property (nonatomic) NSInteger sortOrderIndex; // 1 : 人气 2: 诚信度 3: 最新
@property (nonatomic) NSInteger subHomeIndex;
@property (nonatomic, retain) NSString *choiceCity;
@property (nonatomic, retain) NSString *choiceFamiliarCity;
@property (nonatomic, retain) NSString *choiceEnterpriseCity;
@property (nonatomic, retain) NSString *choiceCommerceCity;
@property (nonatomic, retain) NSString *choiceItemCity;
@property (nonatomic, retain) NSString *choiceServiceCity;

@property (nonatomic, retain) NSString *choiceAkind;
@property (nonatomic, retain) NSString *choiceEnterKind;
@property (nonatomic, retain) NSString *choiceXyleixingIds;
@property (nonatomic, retain) NSString *choiceXyleixingId;
@property (nonatomic, retain) NSString *selectedBusiness;
@property (nonatomic, retain) NSString *choicePleixingIds;
@property (nonatomic, retain) NSString *choiceFenleiIds;


@property (nonatomic, retain) NSString *choiceFamiliarKind;
@property (nonatomic, retain) NSString *choiceEnterpriseKind;
@property (nonatomic, retain) NSString *choiceCommerceKind;
@property (nonatomic, retain) NSString *choiceItemKind;
@property (nonatomic, retain) NSString *choiceServiceKind;

@property (nonatomic, retain) NSString *choiceFamiliarIds;
@property (nonatomic, retain) NSString *choiceEnterpriseIds;
@property (nonatomic, retain) NSString *choiceCommerceIds;
@property (nonatomic, retain) NSString *choiceItemIds;
@property (nonatomic, retain) NSString *choiceServiceIds;
@property (nonatomic, retain) NSString *choiceEvaluateIds;

@property (nonatomic, retain) NSString *choiceCompany;
@property (nonatomic, retain) NSString *choiceCompanyId;
@property (nonatomic, retain) NSString *choiceIds;

@property (nonatomic, retain) NSString *selectedFriendAccountID;
@property (nonatomic, retain) NSString *selectedProductID;
@property (nonatomic, retain) NSDictionary *selectedItemServiceDic;
@property (nonatomic) NSInteger addItemServiceIndex;
@property (nonatomic) NSInteger detailItemServiceIndex;
@property (nonatomic) NSInteger detailFamiliarEnterpriseIndex;
@property (nonatomic, retain) NSString *requestCode;

@property (nonatomic, retain) NSMutableArray *arrayFamiliarHistory;
@property (nonatomic, retain) NSMutableArray *arrayEnterpriseHistory;
@property (nonatomic, retain) NSMutableArray *arrayProductHistory;
@property (nonatomic, retain) NSMutableArray *arrayItemHistory;
@property (nonatomic, retain) NSMutableArray *arrayServiceHistory;
@property (nonatomic, retain) NSMutableArray *arrayCodeHistory;
@property (nonatomic, retain) NSMutableArray *arrayEvaluatePersonalHistory;
@property (nonatomic, retain) NSMutableArray *arrayEvaluateEnterpriseHistory;
@property (nonatomic, retain) NSString *searchFamiliarText;
@property (nonatomic, retain) NSString *searchEnterpriseText;
@property (nonatomic, retain) NSString *searchProductText;
@property (nonatomic, retain) NSString *searchItemText;
@property (nonatomic, retain) NSString *searchServiceText;
@property (nonatomic, retain) NSString *searchCodeText;
@property (nonatomic, retain) NSString *searchEvaluatePersonalText;
@property (nonatomic, retain) NSString *searchEvaluateEnterpriseText;
@property (nonatomic, retain) NSString *lastClick;

@property (nonatomic) long notificationCount;
@property (nonatomic) long myEstimateCnt;
@property (nonatomic) long estimateToMeCnt;
@property (nonatomic) BOOL isPublished;

@property (nonatomic, retain) NSDictionary *selectedEvaluatorDic;
@end
