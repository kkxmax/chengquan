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
@property (nonatomic, strong) IBOutlet UIView *homeChoiceTransView;
@property (nonatomic, strong) IBOutlet UIView *homeChoiceView;
@property (nonatomic, strong) IBOutlet UITabBarItem *homeTabbarItem;
@property (nonatomic, strong) IBOutlet UITabBarItem *hotTabbarItem;
@property (nonatomic, strong) IBOutlet UITabBarItem *evaluateTabbarItem;
@property (nonatomic, strong) IBOutlet UITabBarItem *favouriteTabbarItem;
@property (nonatomic, strong) IBOutlet UITabBarItem *mineTabbarItem;
@end
