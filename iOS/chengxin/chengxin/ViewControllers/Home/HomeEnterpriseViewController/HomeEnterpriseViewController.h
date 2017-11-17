//
//  HomeEnterpriseViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeEnterpriseViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
{
    NSTimer *slideEnterprisePageTimer;
}
@property (nonatomic, strong) IBOutlet UIScrollView *slideEnterpriseScrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *slideEnterprisePageCtrl;
@property (nonatomic, strong) IBOutlet UITableView *homeEnterpriseTableView;

@end
