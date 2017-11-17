//
//  HomeFamiliarViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeFamiliarViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
{
    NSTimer *slideFamiliarPageTimer;
}
@property (nonatomic, strong) IBOutlet UIScrollView *slideFamiliarScrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *slideFamiliarPageCtrl;
@property (nonatomic, strong) IBOutlet UITableView *homeFamiliarTableView;
@end
