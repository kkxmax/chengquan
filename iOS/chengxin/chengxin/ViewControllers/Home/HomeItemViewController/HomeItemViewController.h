//
//  HomeItemViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeItemViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
{
    NSTimer *slideItemPageTimer;
}
@property (nonatomic, strong) IBOutlet UIScrollView *slideItemScrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *slideItemPageCtrl;
@property (nonatomic, strong) IBOutlet UITableView *homeItemTableView;

@end
