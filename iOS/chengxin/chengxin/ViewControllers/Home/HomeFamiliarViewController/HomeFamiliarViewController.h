//
//  HomeFamiliarViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"

@interface HomeFamiliarViewController : BaseViewController<UITableViewDelegate, UITableViewDataSource>
{
}
@property (nonatomic, strong) IBOutlet UITableView *homeFamiliarTableView;

-(void) getFriendFromServer:(NSString*)cityName aKind:(NSString*)akind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword ;
@property (nonatomic) NSInteger currentSortOrderIndex;
- (void)refreshTopItems;
- (void)refreshBottomItems;
@end
