//
//  MainViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MainViewController : UIViewController<UITabBarDelegate>
{
}
@property (nonatomic, strong) IBOutlet UITabBar *mainTabBar;
@property (nonatomic, strong) IBOutlet UIView *mainView;
@property (nonatomic, strong) IBOutlet UIView *transTabBarView;
@property (nonatomic, strong) IBOutlet UIView *homeChoiceBackgroundView;
@property (nonatomic, strong) IBOutlet UIView *homeChoiceView;
@end
