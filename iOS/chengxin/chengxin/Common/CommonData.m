//
//  CommonData.m
//  chengxin
//
//  Created by seniorcoder on 11/7/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "CommonData.h"

@implementation CommonData

+ (instancetype)sharedInstance {
    //Singleton instance
    static CommonData *kCommonData;
    
    //Dispatching it once.
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        
        //  Initializing web api.
        kCommonData = [[self alloc] init];
    });
    
    //Returning kWebAPI.
    return kCommonData;
}

- (id)init {
    self = [super init];
    self.tokenName = @"";
    self.sortOrderIndex = 1;
    self.subHomeIndex = 0;
    self.choiceCity = @"";
    self.choiceFamiliarCity = @"";
    self.choiceEnterpriseCity = @"";
    self.choiceCommerceCity = @"";
    self.choiceItemCity = @"";
    self.choiceServiceCity = @"";
    self.choiceFamiliarKind = @"";
    self.choiceEnterpriseKind = @"";
    self.choiceCommerceKind = @"";
    self.choiceItemKind = @"";
    self.choiceServiceKind = @"";
    self.choiceFamiliarIds = @"";
    self.choiceEnterpriseIds = @"";
    self.choiceCommerceIds = @"";
    self.choiceItemIds = @"";
    self.choiceServiceIds = @"";
    self.choiceEvaluateIds = @"";
    self.choiceCompany = @"";
    self.choiceIds = @"";
    self.searchFamiliarText = @"";
    self.searchEnterpriseText = @"";
    self.searchProductText = @"";
    self.searchItemText = @"";
    self.searchServiceText = @"";
    self.searchCodeText = @"";
    self.searchEvaluatePersonalText = @"";
    self.searchEvaluateEnterpriseText = @"";
    self.arrayFamiliarHistory = [NSMutableArray array];
    self.arrayEnterpriseHistory = [NSMutableArray array];
    self.arrayProductHistory = [NSMutableArray array];
    self.arrayItemHistory = [NSMutableArray array];
    self.arrayServiceHistory = [NSMutableArray array];
    self.arrayCodeHistory = [NSMutableArray array];
    self.arrayEvaluatePersonalHistory = [NSMutableArray array];
    self.arrayEvaluateEnterpriseHistory = [NSMutableArray array];
    self.notificationCount = 0;
    self.myEstimateCnt = 0;
    self.estimateToMeCnt = 0;
    self.selectedEvaluatorDic = nil;
    self.lastClick = @"";
    self.isPublished = false;
    return self;
}

- (void)clearData {
    self.sortOrderIndex = 1;
    self.subHomeIndex = 0;
    self.choiceCity = @"";
    self.choiceFamiliarCity = @"";
    self.choiceEnterpriseCity = @"";
    self.choiceCommerceCity = @"";
    self.choiceItemCity = @"";
    self.choiceServiceCity = @"";
    self.choiceFamiliarKind = @"";
    self.choiceEnterpriseKind = @"";
    self.choiceCommerceKind = @"";
    self.choiceItemKind = @"";
    self.choiceServiceKind = @"";
    self.choiceFamiliarIds = @"";
    self.choiceEnterpriseIds = @"";
    self.choiceCommerceIds = @"";
    self.choiceItemIds = @"";
    self.choiceServiceIds = @"";
    self.choiceEvaluateIds = @"";
    self.choiceCompany = @"";
    self.choiceIds = @"";
    self.searchFamiliarText = @"";
    self.searchEnterpriseText = @"";
    self.searchProductText = @"";
    self.searchItemText = @"";
    self.searchServiceText = @"";
    self.searchCodeText = @"";
    self.searchEvaluateEnterpriseText = @"";
    self.searchEvaluatePersonalText = @"";
    self.arrayFamiliarHistory = [NSMutableArray array];
    self.arrayEnterpriseHistory = [NSMutableArray array];
    self.arrayProductHistory = [NSMutableArray array];
    self.arrayItemHistory = [NSMutableArray array];
    self.arrayServiceHistory = [NSMutableArray array];
    self.arrayCodeHistory = [NSMutableArray array];
    self.arrayEvaluateEnterpriseHistory = [NSMutableArray array];
    self.arrayEvaluatePersonalHistory = [NSMutableArray array];
    self.notificationCount = 0;
    self.myEstimateCnt = 0;
    self.estimateToMeCnt = 0;
    self.selectedEvaluatorDic = nil;
    self.lastClick = @"";
    self.isPublished = false;
}

@end
