//
//  HomeCommerceChoiceViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeCommerceChoiceViewController : UIViewController
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *choiceCityButtons;
@property (nonatomic, strong) IBOutlet UIView *commerceCategoryView;
@end
