//
//  HomeServiceChoiceViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeServiceChoiceViewController : UIViewController
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *choiceCityButtons;
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *choiceCategoryButtons;
@property (weak, nonatomic) IBOutlet UIView *viewContent;

@end
