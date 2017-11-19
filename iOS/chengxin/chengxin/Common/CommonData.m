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
    self.choiceXyleixingIds = @"";
    self.choicePleixingIds = @"";
    self.choiceFenleiIds = @"";
    self.choiceAkind = @"";
    self.choiceCity = @"";
    self.choiceEnterKind = @"";
    self.searchFamiliarText = @"";
    self.searchEnterpriseText = @"";
    self.searchProductText = @"";
    self.searchItemText = @"";
    self.searchServiceText = @"";
    self.searchCodeText = @"";
    self.arrayFamiliarHistory = [NSMutableArray array];
    self.arrayEnterpriseHistory = [NSMutableArray array];
    self.arrayProductHistory = [NSMutableArray array];
    self.arrayItemHistory = [NSMutableArray array];
    self.arrayServiceHistory = [NSMutableArray array];
    self.arrayCodeHistory = [NSMutableArray array];
    return self;
}

@end
