//
//  CategoryHistoryViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CategoryHistoryViewController : UIViewController<UISearchBarDelegate>
{
}
@property (nonatomic) NSInteger curCategoryIndex;
@property (nonatomic, strong) IBOutlet UIView *historyView;
@property (nonatomic, strong) IBOutlet UISearchBar *categorySearchbar;
@end
