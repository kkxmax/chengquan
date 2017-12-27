//
//  HomeEnterpriseViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
@interface HomeEnterpriseViewController : BaseViewController<UITableViewDelegate, UITableViewDataSource>
{
}
@property (nonatomic, strong) IBOutlet UITableView *homeEnterpriseTableView;

- (void)getEnterFromServer:cityName EnterKind:(NSString*)enterKind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword;
@property (nonatomic) NSInteger currentSortOrderIndex;
- (void)refreshTopItems;
- (void)refreshBottomItems;
@end
