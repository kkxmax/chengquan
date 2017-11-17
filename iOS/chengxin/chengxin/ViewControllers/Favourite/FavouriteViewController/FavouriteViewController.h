//
//  FavouriteViewController.h
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

typedef enum {
    em_WoDeGuanZhu,
    em_ChengXinLian
} Favourite_Tab;
@interface FavouriteViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, NotificationDelegate>
{
    Favourite_Tab tab_selection;
}
@property (nonatomic, retain) IBOutlet UIScrollView* myFavouriteContentsView;
@property (nonatomic, retain) IBOutlet UIScrollView* chengxinlianContentsView;
@property (nonatomic, retain) IBOutlet UITableView* favouriteItemTableView;
@property (nonatomic, retain) IBOutlet UITableView* chengxinlianItemTableView;
@property (nonatomic, retain) IBOutlet UIView* contentsContainer;
@property (nonatomic, retain) IBOutlet UIButton* btnMyFavourite;
@property (nonatomic, retain) IBOutlet UIButton* btnChengXinLian;
@property (nonatomic, retain) IBOutlet UILabel* lblMyFavouriteUnderline;
@property (nonatomic, retain) IBOutlet UILabel* lblChengXinLianUnderline;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCount;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCount;
@property (nonatomic, retain) IBOutlet UILabel* lblMyAncestorCount;
@property (nonatomic, retain) IBOutlet UILabel* lblFriend1Count;
@property (nonatomic, retain) IBOutlet UILabel* lblFriend2Count;
@property (nonatomic, retain) IBOutlet UILabel* lblFriend3Count;
@property (weak, nonatomic) IBOutlet UIView *viewBlank;
@property (weak, nonatomic) IBOutlet UIView *viewNoNetwork;
@property (weak, nonatomic) IBOutlet UIView *viewBlank2;
@property (weak, nonatomic) IBOutlet UIView *viewNoNetwork2;
@property (nonatomic, strong) IBOutlet UILabel *messageNumberLabel;


-(IBAction)onMyFavourite:(id)sender;
-(IBAction)onTrustSeries:(id)sender;
-(IBAction)onEnterprise:(id)sender;
-(IBAction)onPersonal:(id)sender;
-(IBAction)onMyHome:(id)sender;
-(IBAction)onFriends:(id)sender;
@end
