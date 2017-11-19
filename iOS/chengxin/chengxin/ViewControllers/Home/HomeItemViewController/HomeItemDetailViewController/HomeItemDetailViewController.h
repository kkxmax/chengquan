//
//  HomeItemDetailViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeItemDetailViewController : UIViewController
{
}
@property (nonatomic, strong) IBOutlet UIScrollView *detailScrollView;
@property (nonatomic, weak) IBOutlet UIImageView *avatarImageView;
@property (nonatomic, weak) IBOutlet UILabel *logoImageLabel;
@property (nonatomic, weak) IBOutlet UILabel *nameLabel;
@property (nonatomic, weak) IBOutlet UIButton *fenleiNameButton;
@property (nonatomic, weak) IBOutlet UILabel *addressLabel;
@property (nonatomic, weak) IBOutlet UILabel *codeLabel;
@property (nonatomic, weak) IBOutlet UITextView *commentTextView;
@property (nonatomic, weak) IBOutlet UILabel *networkAddrLabel;
@property (nonatomic, weak) IBOutlet UILabel *needLabel;
@property (nonatomic, weak) IBOutlet UILabel *contactNameLabel;
@property (nonatomic, weak) IBOutlet UILabel *contactMobileLabel;
@property (nonatomic, weak) IBOutlet UILabel *contactWeixinLabel;
@property (nonatomic, weak) IBOutlet UIImageView *accountLogoImageView;
@property (nonatomic, weak) IBOutlet UILabel *accountLogoImageLabel;
@property (nonatomic, weak) IBOutlet UILabel *officeMarkLabel;
@property (nonatomic, weak) IBOutlet UILabel *enterNameLabel;
@property (nonatomic, weak) IBOutlet UILabel *enterCodeLabel;
@property (nonatomic, weak) IBOutlet UILabel *creditLabel;
@property (nonatomic, weak) IBOutlet UILabel *writeTimeLabel;

@end
