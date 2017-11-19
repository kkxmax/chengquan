//
//  CategoryHistoryViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CategoryHistoryViewController : UIViewController<UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate>
{
}
@property (nonatomic) NSInteger curCategoryIndex;
@property (nonatomic, strong) IBOutlet UITableView *searchTableView;
@property (nonatomic, strong) IBOutlet UISearchBar *categorySearchbar;
@end
