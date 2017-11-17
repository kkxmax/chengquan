//
//  HomeServiceViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeServiceViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
{
    NSTimer *slideServicePageTimer;
}
@property (nonatomic, strong) IBOutlet UIScrollView *slideServiceScrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *slideServicePageCtrl;
@property (nonatomic, strong) IBOutlet UITableView *homeServiceTableView;


@end
