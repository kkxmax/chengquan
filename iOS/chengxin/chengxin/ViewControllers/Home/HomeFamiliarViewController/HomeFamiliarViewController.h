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
}
@property (nonatomic, strong) IBOutlet UITableView *homeFamiliarTableView;
@property (nonatomic, strong) IBOutlet UIActivityIndicatorView *indicatorView;

-(void) getFriendFromServer:(NSString*)cityName aKind:(NSString*)akind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword ;
@property (nonatomic) NSInteger currentSortOrderIndex;
@end
