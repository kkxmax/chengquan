//
//  FavouritesTableViewController.h
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FavouritesTableViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, retain) IBOutlet UITableView* tableView;
@end
