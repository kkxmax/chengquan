//
//  HomeServiceViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
@interface HomeServiceViewController : BaseViewController<UITableViewDelegate, UITableViewDataSource>
{
}
@property (nonatomic, strong) IBOutlet UITableView *homeServiceTableView;

- (void)getServiceFromServer:cityName AKind:(NSString*)akind FenleiIds:(NSString*)fenleiIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword;
@property (nonatomic) NSInteger currentSortOrderIndex;

- (void)refreshTopItems;
- (void)refreshBottomItems;
- (void)addAction;
@end
