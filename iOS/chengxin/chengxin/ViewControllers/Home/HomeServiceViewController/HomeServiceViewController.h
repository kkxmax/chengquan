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
}
@property (nonatomic, strong) IBOutlet UITableView *homeServiceTableView;
@property (nonatomic, weak) IBOutlet UIActivityIndicatorView *indicatorView;

- (void)getServiceFromServer:cityName AKind:(NSString*)akind FenleiIds:(NSString*)fenleiIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword;
@property (nonatomic) NSInteger currentSortOrderIndex;

@property (nonatomic, strong) IBOutlet UIButton *addButton;

@end
