//
//  HomeChoiceViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomePersonalChoiceViewController : UIViewController
{
}
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *choiceCityButtons;
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *choiceCategoryButtons;

@end
