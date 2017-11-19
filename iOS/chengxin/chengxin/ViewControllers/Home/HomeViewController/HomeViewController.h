//
//  HomeViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/23/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"
#import "AppDelegate.h"
@interface HomeViewController : UIViewController<UIScrollViewDelegate, NotificationDelegate>
{
    NSTimer *slideHomePageTimer;
}
@property (nonatomic, strong) IBOutlet UISearchBar *searchBar;
@property (nonatomic, strong) IBOutlet UILabel *messageNumberLabel;
@property (nonatomic, strong) IBOutlet UIScrollView *homeScrollView;
@property (nonatomic, strong) IBOutlet UIButton *familiarButton;
@property (nonatomic, strong) IBOutlet UIButton *enterpriseButton;
@property (nonatomic, strong) IBOutlet UIButton *commerceButton;
@property (nonatomic, strong) IBOutlet UIButton *itemButton;
@property (nonatomic, strong) IBOutlet UIButton *serviceButton;
@property (nonatomic, strong) IBOutlet UIButton *sortHomeButton;
@property (nonatomic, strong) IBOutlet UIView *overScrollView;

@property (nonatomic, strong) IBOutlet UIScrollView *slideHomeScrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *slideHomePageCtrl;
@property (nonatomic, strong) IBOutlet UIActivityIndicatorView *indicatorView;
@end
