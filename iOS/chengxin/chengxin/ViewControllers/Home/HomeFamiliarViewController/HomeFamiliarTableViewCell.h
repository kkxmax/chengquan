//
//  HomeFamiliarTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeFamiliarTableViewCell : UITableViewCell
{
}
@property (nonatomic, strong) NSString *accountID;

@property (nonatomic, strong) IBOutlet UIImageView *logoImageView;
@property (nonatomic, strong) IBOutlet UIImageView *logoEmptyRealImageView;
@property (nonatomic, strong) IBOutlet UILabel *logoEmptyEnterpriseLabel;
@property (nonatomic, strong) IBOutlet UIImageView *markImageView;
@property (nonatomic, strong) IBOutlet UILabel *nameLabel;
@property (nonatomic, strong) IBOutlet UILabel *codeLabel;
@property (nonatomic, strong) IBOutlet UIButton *xyNameButton;
@property (nonatomic, strong) IBOutlet UILabel *reqCodeSenderLabel;
@property (nonatomic, strong) IBOutlet UILabel *reqTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *productsLabel;
@property (nonatomic, strong) IBOutlet UILabel *productsTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *itemLabel;
@property (nonatomic, strong) IBOutlet UILabel *itemTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *serviceLabel;
@property (nonatomic, strong) IBOutlet UILabel *serviceTitleLabel;
@property (nonatomic, strong) IBOutlet UILabel *electCountLabel;
@property (nonatomic, strong) IBOutlet UILabel *feedbackCountLabel;
@property (nonatomic, strong) IBOutlet UILabel *viewCountLabel;
@property (nonatomic, strong) IBOutlet UIButton *interestedButton;
@property (nonatomic, strong) IBOutlet UIView *productView;
@property (nonatomic, strong) IBOutlet UIView *itemView;
@property (nonatomic, strong) IBOutlet UIView *serviceView;
@property (nonatomic, strong) IBOutlet UIView *reqView;

@property (nonatomic, strong) IBOutlet NSLayoutConstraint *productViewHeightConstraint;
@property (nonatomic, strong) IBOutlet NSLayoutConstraint *itemViewHeightConstraint;
@property (nonatomic, strong) IBOutlet NSLayoutConstraint *serviceViewHeightConstraint;
@property (nonatomic, strong) IBOutlet NSLayoutConstraint *reqViewHeightConstraint;

@end
